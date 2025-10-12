package com.example.demolombok;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Personnebuilder {
	private final String nom;
    private final String prenom;
    private final int age;
}
