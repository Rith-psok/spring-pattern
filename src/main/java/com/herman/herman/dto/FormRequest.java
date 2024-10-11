package com.herman.herman.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FormRequest {
  private String type;
  private String placeHolder;
  private String fieldName;
  private String value;
  private int x;
  private int y;
  private int width;
  private int height;
  private boolean required;
  private boolean showOptionText;
  private List<RadioBtnDto> radioBtnDtoList;
}
