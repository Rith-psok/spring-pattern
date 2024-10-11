package com.herman.herman.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RadioBtnDto {
  private int x;
  private int y;
  private String group;
  private String option;
}
