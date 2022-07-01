
package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.integration.EmployeService;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import javax.persistence.EntityExistsException;

import static org.hibernate.bytecode.BytecodeLogger.LOGGER;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {
    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;
    /*    @Mock
        private DummyService dummyService;
        @Test
        public void testDummy(){
            //Given
            when(dummyService.doSomething()).thenReturn(5);
            //When
            Boolean b = employeService.executeDummy();
            //Then
            Assertions.assertThat(b).isTrue();
        }*/
    @Test
    public void testEmbaucheEmployeLimiteMatricule(){
        //Given
        when(employeRepository.findLastMatricule()).thenReturn("99999");
        //When
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);
        });
        //Then
        Assertions.assertThat(t).isInstanceOf(EmployeException.class)
                .hasMessage("Limite des 100000 matricules atteinte !");
    }
    @Test
    public void testEmbaucheEmployeEmployeExist(){
        //Given
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("C00001")).thenReturn(new Employe());
        //When
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);
        });
        //Then
        Assertions.assertThat(t).isInstanceOf(EntityExistsException.class)
                .hasMessage("L'employé de matricule C00001 existe déjà en BDD");
        LOGGER.warn("L'employée existe déjà en BDD");;
    }
    @Test
    public void testEmbaucheEmploye() throws EmployeException {
        //Given
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        Mockito.when(employeRepository.findByMatricule("C00001")).thenReturn(null);
//        Mockito.when(employeRepository.save(null)).thenReturn();
        //When
        employeService.embaucheEmploye("Doe", "John", Poste.COMMERCIAL, NiveauEtude.MASTER, 1.0);
        //Then
//        Employe employe = employeRepository.findByMatricule("C00001");
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe.getNom()).isEqualTo("Doe");
        Assertions.assertThat(employe.getPrenom()).isEqualTo("John");
        Assertions.assertThat(employe.getMatricule()).isEqualTo("C00001");
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getPerformance()).isEqualTo(Entreprise.PERFORMANCE_BASE);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(2129.71);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1);
    }

    // calculPerformanceCommercial
    @Test
    void testCalculPerformanceCommercialWithCaTraiteNegatifOrNull(){
        //given

        //when
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial("C12345", (long) -1, (long) 1);
        });

        //then
        Assertions.assertThat(t).isInstanceOf(EmployeException.class).hasMessage("Le chiffre d'affaire traité ne peut être négatif ou null !");
    }

    @Test
    void testCalculPerformanceCommercialWithObjectifCaNegatifOrNull(){
        //given

        //when
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial("C12345", (long) 1, (long) -1);
        });

        //then
        Assertions.assertThat(t).isInstanceOf(EmployeException.class).hasMessage("L'objectif de chiffre d'affaire ne peut être négatif ou null !");
    }

    @Test
    void testCalculPerformanceCommercialWithMatriculeNotExisting(){
        //given
        Mockito.when(employeRepository.findByMatricule("C12345")).thenReturn(null);

        //when
        Throwable t = Assertions.catchThrowable(() -> {
            employeService.calculPerformanceCommercial("C12345", (long) 1, (long) 1);
        });

        //then
        Assertions.assertThat(t).isInstanceOf(EmployeException.class).hasMessage("Le matricule C12345 n'existe pas !");
    }

    @ParameterizedTest
    @CsvSource({
            "10,1",
            "90,2"
    })
    void testCalculPerformanceCommercial(Long CaTraite, Integer performance) throws EmployeException {
        //given
        Mockito.when(employeRepository.findByMatricule("C54321")).thenReturn(
                new Employe("Amine","Gouiri","C54321",LocalDate.now(),500.0, 10,1.0
                ));
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(1.0);

        //when
        employeService.calculPerformanceCommercial("C54321", CaTraite, 100L);

        //then
        ArgumentCaptor<Employe> employeCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository, Mockito.times(1)).save(employeCaptor.capture());
        Assertions.assertThat(employeCaptor.getValue().getPerformance()).isEqualTo(performance);
    }

}
