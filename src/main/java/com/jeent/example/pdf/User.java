package com.jeent.example.pdf;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class User {
	private Integer id;
	private String name;
}
