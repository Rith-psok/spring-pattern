package com.herman.herman.service;

import com.herman.herman.constant.PdfFormTypeConstant;
import com.herman.herman.dto.FileResponse;
import com.herman.herman.dto.FormRequest;
import com.herman.herman.dto.RadioBtnDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.apache.pdfbox.cos.COSDictionary;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDAppearanceContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceCharacteristicsDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceDictionary;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAppearanceEntry;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PdfService {
  private static final String OUTPUT_PATH = "D:\\self\\pdf";
  private static final String TEMP_PATH = "D:\\self\\pdf\\temp\\";
  private static final Path SAVE_DIRECTORY = Paths.get(OUTPUT_PATH).resolve(String.format("%s%s", UUID.randomUUID(), ".pdf"));

  public FileResponse generatePdf(FormRequest formRequest) {
    PDDocument document = new PDDocument();

    PDPage page = new PDPage(PDRectangle.A4);
    document.addPage(page);

    var fileRes = new FileResponse();
    switch (formRequest.getType()) {
      case PdfFormTypeConstant.RADIO_BTN -> fileRes = createPdfRadioBtn(formRequest, document, page);
      case PdfFormTypeConstant.TEXTBOX -> fileRes = createTextBox(formRequest, document, page);
      default -> throw new RuntimeException("Given type is not correct.");
    }

    return fileRes;
  }

  private FileResponse createTextBox(FormRequest formRequest, PDDocument document, PDPage page) {
    // Create an AcroForm
    PDAcroForm acroForm = new PDAcroForm(document);
    document.getDocumentCatalog().setAcroForm(acroForm);

    // Define and add a text field
    PDFont font = new PDType1Font(Standard14Fonts.FontName.valueOf("HELVETICA"));

    // Write some text to the PDF
    try {
      PDPageContentStream contentStream = new PDPageContentStream(document, page);
      contentStream.beginText();
      contentStream.setFont(font, 12);
      contentStream.newLineAtOffset(100, 730);
      contentStream.showText("Fill out the form:");
      contentStream.endText();

      contentStream.close();
      document.save(SAVE_DIRECTORY.toString());
      document.close();

      final var generatedFile = new File(SAVE_DIRECTORY.toString());
      return FileResponse.builder()
          .fileName(generatedFile.getName())
          .contentType(MediaType.APPLICATION_PDF_VALUE)
          .size(generatedFile.length())
          .resource(Files.readAllBytes(SAVE_DIRECTORY))
          .build();
    } catch (IOException ex) {
      throw new RuntimeException();
    }
  }


  private FileResponse createPdfRadioBtn(FormRequest formRequest, PDDocument document, PDPage page) {
    try {
      PDAcroForm acroForm = new PDAcroForm(document);
      document.getDocumentCatalog().setAcroForm(acroForm);
      acroForm.setNeedAppearances(true);
      acroForm.setXFA(null);

      PDFont font = new PDType1Font(Standard14Fonts.FontName.valueOf("HELVETICA"));

      PDResources res = new PDResources();
      COSName fontName = res.add(font);
      acroForm.setDefaultResources(res);
      acroForm.setDefaultAppearance("/" + fontName.getName() + " 10 Tf 0 g");

      PDPageContentStream contentStream = new PDPageContentStream(document, page);

      Map<String, List<RadioBtnDto>> groupOptions = new HashMap<>();
      for (final var radioBtn : formRequest.getRadioBtnDtoList()) {
        groupOptions.computeIfAbsent(radioBtn.getGroup(), k -> new ArrayList<>()).add(radioBtn);
      }

      for (final var groupOption : groupOptions.entrySet()) {
        final var groupName = groupOption.getKey();
        final var options = groupOption.getValue();
        PDRadioButton radioButton = new PDRadioButton(acroForm);
        List<PDAnnotationWidget> widgets = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
          final var activeRadioBtn = options.get(i);
          radioButton.setPartialName(groupName + "_" + activeRadioBtn.getOption());
          radioButton.setExportValues(options.stream().map(RadioBtnDto::getOption).toList());
          radioButton.getCOSObject().setName(COSName.DV, activeRadioBtn.getOption());
          PDAppearanceCharacteristicsDictionary fieldAppearance = new PDAppearanceCharacteristicsDictionary(new COSDictionary());
          fieldAppearance.setBorderColour(new PDColor(new float[]{0, 0, 0}, PDDeviceRGB.INSTANCE));

          PDAnnotationWidget widget = new PDAnnotationWidget();
          widget.setRectangle(new PDRectangle(activeRadioBtn.getX(), activeRadioBtn.getY(), 16, 16));
          widget.setAppearanceCharacteristics(fieldAppearance);

          widgets.add(widget);
          page.getAnnotations().add(widget);

          // Without it Adobe does not set the values properly
          PDAppearanceDictionary appearance = new PDAppearanceDictionary();
          COSDictionary dict = new COSDictionary();
          dict.setItem(COSName.getPDFName("Off"), new COSDictionary());
          PDAppearanceEntry appearanceEntry = new PDAppearanceEntry(dict);
          appearance.setNormalAppearance(appearanceEntry);
          widget.setAppearance(appearance);

          if (formRequest.isShowOptionText()) {
            contentStream.beginText();
            contentStream.setFont(font, 10);
            contentStream.newLineAtOffset(activeRadioBtn.getX() + 20, activeRadioBtn.getY() + 4);
            contentStream.showText(activeRadioBtn.getOption());
            contentStream.endText();
          }
        }
        radioButton.setWidgets(widgets);

        acroForm.getFields().add(radioButton);
      }
      contentStream.close();
      document.save(SAVE_DIRECTORY.toString());
      document.close();

      final var generatedFile = new File(SAVE_DIRECTORY.toString());
      return FileResponse.builder()
          .fileName(generatedFile.getName())
          .contentType(MediaType.APPLICATION_PDF_VALUE)
          .size(generatedFile.length())
          .resource(Files.readAllBytes(SAVE_DIRECTORY))
          .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public FileResponse imageToPdf(MultipartFile file) {
    try {
      PDDocument document = new PDDocument();

      PDPage page = new PDPage(PDRectangle.A4);
      document.addPage(page);
      // add image
      final var image = new File(Paths.get(TEMP_PATH).resolve(Objects.requireNonNull(file.getOriginalFilename())).toString());
      file.transferTo(image);
      PDImageXObject pdImage = PDImageXObject.createFromFileByContent(image, document);
      this.addImage(pdImage, document, page);

      document.save(SAVE_DIRECTORY.toString());
      document.close();

      final var generatedFile = new File(SAVE_DIRECTORY.toString());
      return FileResponse.builder()
          .fileName(generatedFile.getName())
          .contentType(MediaType.APPLICATION_PDF_VALUE)
          .size(generatedFile.length())
          .resource(Files.readAllBytes(SAVE_DIRECTORY))
          .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void addImage(PDImageXObject pdImage, PDDocument document, PDPage page) {
    try {
      final var contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
      // Calculate image dimensions
      float imageWidth = pdImage.getWidth();
      float imageHeight = pdImage.getHeight();
      float boxWidth = document.getPage(0).getMediaBox().getWidth();
      // Set desired height
      float newImageHeight = (imageHeight * boxWidth) / imageWidth;

      contentStream.drawImage(pdImage, 0, 0, boxWidth, newImageHeight);
      contentStream.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
