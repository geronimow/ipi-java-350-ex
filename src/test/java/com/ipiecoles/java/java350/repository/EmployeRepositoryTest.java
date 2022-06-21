package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.time.LocalDate;
import java.util.Arrays;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @AfterEach
    @BeforeEach
    public void purge(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatriculeWithoutEmploye(){

        //Given

        //When
        String matricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(matricule).isNull();
    }

    @Test
    public void testFindLastMatriculeWithEmploye(){

        //Given (données rentrées pour le test)
        Employe employe = new Employe("Doe", "John", "M123456", LocalDate.now(), 2500d, 1, 1.0);

        //When
        String matricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(matricule).isEqualTo(null);
    }

    @Test
    public void testFindLastMatriculeWith3Employe(){

        //Given (données rentrées pour le test)
        employeRepository.deleteAll();
        Employe employe = new Employe("Doe", "John", "M123456", LocalDate.now(), 2500d, 1, 1.0);
        Employe employe2 = new Employe("Bruce", "Lee", "M123456", LocalDate.now(), 2500d, 1, 1.0);
        Employe employe3 = new Employe("Rolls", "Royce", "T123456", LocalDate.now(), 2500d, 1, 1.0);
        employeRepository.saveAll(Arrays.asList(employe, employe2, employe3));

        //When
        String matricule = employeRepository.findLastMatricule();

        //Then
        Assertions.assertThat(matricule).isEqualTo("123456");
    }
}
