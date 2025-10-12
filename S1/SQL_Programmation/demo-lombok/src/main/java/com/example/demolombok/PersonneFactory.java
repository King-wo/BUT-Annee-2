package com.example.demolombok;

import lombok.Builder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PersonneFactory 
{	
	@Builder(builderClassName="FullBuilder",
                 builderMethodName="fullBuilder")
	public Personnebuilder personnebf(String nom, 
					   String prenom, 
					   int age)
	{
		return Personnebuilder.builder().nom(nom)
					 .prenom(prenom)
					 .age(age)
					 .build();
	}
}
