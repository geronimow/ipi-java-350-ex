package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then nbAnneesAnciennete = 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @Test
    public void testGetNbAnneesAncienneteDateEmbauchePassee(){
        //Given
        //Date d'embauche 10 ans dans le passé
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(10));
        //employe.setDateEmbauche(LocalDate.of(2012, 4, 26)); //Pas bon...

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        // => 10
        Assertions.assertThat(nbAnneesAnciennete).isEqualTo(10);
    }

    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheFuture(){
        //Given
        //Date d'embauche 2 ans dans le futur
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(2));

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @Test
    public void testGetNbAnneesAncienneteDateEmbaucheNull(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        // => 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }

    @ParameterizedTest
    @CsvSource({
            "'M12345',0,1,1.0,1700.0",
            "'T12346',0,1,1.0,1000.0",
            "'T12346',0,2,1.0,2300.0",
            ",0,1,1.0,1000.0",
            "'T12346',0,,1.0,1000.0"
    })
    public void testGetPrimeAnnuelleManagerPerformanceBasePleinTemps(
            String matricule,
            Integer nbAnneesAnciennete,
            Integer performance,
            Double tauxActivite,
            Double prime
    ){
        //Given
        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneesAnciennete), 2500d, performance, tauxActivite);

        //When
        Double primeObtenue = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertThat(primeObtenue).isEqualTo(prime);
    }

    @Test
    public void testAugmenterSalaireDe10Pourcente() throws EmployeException {
        //Given
        Employe employe = new Employe();

        //When
        try {
            employe.augmenterSalaire(0.1);
        } catch (EmployeException a) {
            a.printStackTrace();
        }

        //Then
        System.out.println(employe.getSalaire());
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1673.342);
    }

    @Test
    public void testAugmenterSalaire60Pourcents() throws EmployeException {
        //Given
        Employe employe = new Employe();

        //When
        Throwable a = Assertions.catchThrowable(() -> {
            employe.augmenterSalaire(0.6);
        });

        //Then
        System.out.println(employe.getSalaire());
        Assertions.assertThat(a).isInstanceOf(EmployeException.class).hasMessage("Le pourcentage ne doit pas être supérieur à 0.5");
    }

    @Test
    public void testAugmenterSalaireNégatif() throws EmployeException {
        //Given
        Employe employe = new Employe();

        //When
        Throwable a = Assertions.catchThrowable(() -> {
            employe.augmenterSalaire(-0.2);
        });

        //Then
        System.out.println(employe.getSalaire());
        Assertions.assertThat(a).isInstanceOf(EmployeException.class).hasMessage("Le pourcentage ne peut pas être négatif");
    }

    @Test
    public void testAugmenterSalaireNull() throws EmployeException {
        //Given
        Employe employe = new Employe();
        employe.setSalaire(null);

        //When
        Throwable a = Assertions.catchThrowable(() -> {
            employe.augmenterSalaire(0);
        });

        //Then
        System.out.println(employe.getSalaire());
        Assertions.assertThat(a).isInstanceOf(EmployeException.class).hasMessage("Le salaire est null");
    }

    @Test
    public void getNbRtt(){
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nbAnneesAnciennete = employe.getNombreAnneeAnciennete();

        //Then nbAnneesAnciennete = 0
        Assertions.assertThat(nbAnneesAnciennete).isZero();
    }


}