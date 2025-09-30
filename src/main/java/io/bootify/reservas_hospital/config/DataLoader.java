package io.bootify.reservas_hospital.config;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.ServiciosMedico;
import io.bootify.reservas_hospital.repos.DoctorRepository;
import io.bootify.reservas_hospital.repos.ServiciosMedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ServiciosMedicoRepository serviciosMedicoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen datos
        if (doctorRepository.count() == 0) {
            crearDoctores();
        }
        
        if (serviciosMedicoRepository.count() == 0) {
            crearServiciosMedicos();
        }
        
        System.out.println("Datos de prueba creados exitosamente");
        System.out.println("Doctores en BD: " + doctorRepository.count());
        System.out.println("Servicios en BD: " + serviciosMedicoRepository.count());
    }

    private void crearDoctores() {
    Doctor doctor1 = new Doctor();
    doctor1.setNombreCompleto("Dr. Fernando Torres");
    doctor1.setEspecialidad("Neurología");
    doctor1.setEmail("fernando.torres@clinica.com");
    doctor1.setTelefono("3001112233");
    doctor1.setActivo(true);
    doctorRepository.save(doctor1);

    Doctor doctor2 = new Doctor();
    doctor2.setNombreCompleto("Dra. Sofia Ramirez");
    doctor2.setEspecialidad("Oftalmología");
    doctor2.setEmail("sofia.ramirez@clinica.com");
    doctor2.setTelefono("3012223344");
    doctor2.setActivo(true);
    doctorRepository.save(doctor2);

    Doctor doctor3 = new Doctor();
    doctor3.setNombreCompleto("Dr. Andres Castillo");
    doctor3.setEspecialidad("Ortopedia");
    doctor3.setEmail("andres.castillo@clinica.com");
    doctor3.setTelefono("3023334455");
    doctor3.setActivo(true);
    doctorRepository.save(doctor3);

    Doctor doctor4 = new Doctor();
    doctor4.setNombreCompleto("Dra. Valentina Herrera");
    doctor4.setEspecialidad("Psiquiatría");
    doctor4.setEmail("valentina.herrera@clinica.com");
    doctor4.setTelefono("3034445566");
    doctor4.setActivo(true);
    doctorRepository.save(doctor4);

    Doctor doctor5 = new Doctor();
    doctor5.setNombreCompleto("Dr. Sebastian Gomez");
    doctor5.setEspecialidad("Medicina Interna");
    doctor5.setEmail("sebastian.gomez@clinica.com");
    doctor5.setTelefono("3045556677");
    doctor5.setActivo(true);
    doctorRepository.save(doctor5);
}


    private void crearServiciosMedicos() {
        // Crear servicios médicos de ejemplo
        ServiciosMedico servicio1 = new ServiciosMedico();
        servicio1.setNombreServicio("Consulta General");
        servicio1.setDuracionMinutos(30);
        servicio1.setDescripcion("Consulta médica general para evaluación de salud");
        servicio1.setActivo(true);
        serviciosMedicoRepository.save(servicio1);

        ServiciosMedico servicio2 = new ServiciosMedico();
        servicio2.setNombreServicio("Consulta Especializada");
        servicio2.setDuracionMinutos(45);
        servicio2.setDescripcion("Consulta con médico especialista");
        servicio2.setActivo(true);
        serviciosMedicoRepository.save(servicio2);

        ServiciosMedico servicio3 = new ServiciosMedico();
        servicio3.setNombreServicio("Chequeo Anual");
        servicio3.setDuracionMinutos(60);
        servicio3.setDescripcion("Chequeo médico completo anual");
        servicio3.setActivo(true);
        serviciosMedicoRepository.save(servicio3);

        ServiciosMedico servicio4 = new ServiciosMedico();
        servicio4.setNombreServicio("Vacunación");
        servicio4.setDuracionMinutos(15);
        servicio4.setDescripcion("Aplicación de vacunas");
        servicio4.setActivo(true);
        serviciosMedicoRepository.save(servicio4);

        ServiciosMedico servicio5 = new ServiciosMedico();
        servicio5.setNombreServicio("Examen de Laboratorio");
        servicio5.setDuracionMinutos(20);
        servicio5.setDescripcion("Toma de muestras para análisis de laboratorio");
        servicio5.setActivo(true);
        serviciosMedicoRepository.save(servicio5);
    }
}