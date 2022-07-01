package com.ipiecoles.java.java350.integration;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;

import com.ipiecoles.java.java350.service.EmployeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class) // Junit 5
@SpringBootTest
public class EmployeServiceIntegrationTest {

    @Autowired
    EmployeRepository employeRepository;

    @Autowired
    EmployeService employeService;

    @BeforeEach
    public void delete(){
        employeRepository.deleteAll();
    }

    @Test
    void testEmbaucheEmployeWithoutEmploye() throws EmployeException {
        //given

        //when
        employeService.embaucheEmploye("oui","non", Poste.TECHNICIEN, NiveauEtude.BAC,1.0);
        List<Employe> employes = employeRepository.findAll();
        Employe employe = employes.get(0);

        //then
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    @Test
    void testEmbaucheEmployeWithEmploye() throws EmployeException {
        //given
        Employe employeBefore = new Employe("ooo","nnn","M11111", LocalDate.now(),8000.0,4,1.0);
        employeRepository.save(employeBefore);

        //when
        employeService.embaucheEmploye("oui","non", Poste.TECHNICIEN, NiveauEtude.BAC,1.0);


        //then
        Employe employe = employeRepository.findByMatricule("T11112");
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T11112");
        Assertions.assertThat(employe.getNom()).isEqualTo("oui");
        Assertions.assertThat(employe.getPrenom()).isEqualTo("non");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1673.34);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1);
    }


    @Test
    void testCalculPerformanceCommercial() throws EmployeException {
        //given
        Employe employe = new Employe("Doe","John","C12345", LocalDate.now(),1000.0,5,1.0);
        employeRepository.save(employe);

        //when
        employeService.calculPerformanceCommercial("C12345", (long) 10, (long) 10);


        //then
        Employe employeNew = employeRepository.findByMatricule("C12345");
        Assertions.assertThat(employeNew.getPerformance()).isEqualTo(5);
    }

    @Test
    void avgPerformanceWhereMatriculeStartsWith() throws EmployeException {
        //given
        Employe employe = new Employe("Doe","John","C12345", LocalDate.now(),1000.0,5,1.0);
        employeRepository.save(employe);

        //when
        employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //then
        Assertions.assertThat(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).isEqualTo(5);
    }

}