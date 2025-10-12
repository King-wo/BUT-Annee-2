package com.example.demolombok;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoLombokApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoLombokApplication.class, args);

        System.out.println("---------------------------");

        // Méthode "classique"
        Personne personne = new Personne();
        personne.setPrenom("Peter");
        personne.setNom("Milanovich");
        personne.setAge(20);

        System.out.println("Prenom : "+personne.getPrenom());
        System.out.println("Nom : "+personne.getNom());
        System.out.println("Age : "+personne.getAge());
        System.out.println(personne);

        System.out.println("---------------------------");

        // Méthode avec lombok et l'annotation @Data
        Personnelombok personnel = new Personnelombok();
        personnel.setPrenom("Peter");
        personnel.setNom("Milanovich");
        personnel.setAge(20);

        System.out.println("Prenom : "+personnel.getPrenom());
        System.out.println("Nom : "+personnel.getNom());
        System.out.println("Age : "+personnel.getAge());
        System.out.println(personnel);

        System.out.println("---------------------------");

        // Méthode avec lombok avec l'annotation @Builder et autres
        Personnebuilder personneb = Personnebuilder.builder()
                .prenom("Peter")
                .nom("Milanovitch")
                .age(20)
                .build();

        System.out.println("Prenom : "+personneb.getPrenom());
        System.out.println("Nom : "+personneb.getNom());
        System.out.println("Age : "+personneb.getAge());
        System.out.println(personneb);

        System.out.println("---------------------------");

        Personnebuilder personnebf = PersonneFactory.fullBuilder()
                .nom("Henri")
                .prenom("Alex")
                .age(25)
                .build();
        System.out.println(personnebf);
    }

}
