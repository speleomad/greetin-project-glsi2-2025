package com.fsb.greeting.web.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
  private Long id;
  private String name;
  private short age;
  private String photo;  
}