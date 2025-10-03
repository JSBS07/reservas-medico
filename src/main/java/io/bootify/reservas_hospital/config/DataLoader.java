package io.bootify.reservas_hospital.config;

import io.bootify.reservas_hospital.domain.Doctor;
import io.bootify.reservas_hospital.domain.ServiciosMedico;
import io.bootify.reservas_hospital.domain.Usuario;
import io.bootify.reservas_hospital.repos.DoctorRepository;
import io.bootify.reservas_hospital.repos.ServiciosMedicoRepository;
import io.bootify.reservas_hospital.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private ServiciosMedicoRepository serviciosMedicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen datos
        if (doctorRepository.count() == 0) {
            crearDoctores();
        }
        
        if (serviciosMedicoRepository.count() == 0) {
            crearServiciosMedicos();
        }
        
        if (usuarioRepository.count() == 0) {
            crearUsuariosPrueba();
        }
        
        System.out.println(" Datos de prueba creados exitosamente");
        System.out.println(" Doctores en BD: " + doctorRepository.count());
        System.out.println(" Servicios en BD: " + serviciosMedicoRepository.count());
        System.out.println(" Usuarios en BD: " + usuarioRepository.count());
    }

    private void crearDoctores() {
        // Crear doctores de ejemplo
        Doctor doctor1 = new Doctor();
        doctor1.setNombreCompleto("Dr. Juan Pérez");
        doctor1.setEspecialidad("Cardiología");
        doctor1.setEmail("juan.perez@clinica.com");
        doctor1.setTelefono("+57 123 456 7890");
        doctor1.setActivo(true);
        doctorRepository.save(doctor1);

        Doctor doctor2 = new Doctor();
        doctor2.setNombreCompleto("Dra. María García");
        doctor2.setEspecialidad("Pediatría");
        doctor2.setEmail("maria.garcia@clinica.com");
        doctor2.setTelefono("+57 098 765 4321");
        doctor2.setActivo(true);
        doctorRepository.save(doctor2);

        Doctor doctor3 = new Doctor();
        doctor3.setNombreCompleto("Dr. Carlos López");
        doctor3.setEspecialidad("Dermatología");
        doctor3.setEmail("carlos.lopez@clinica.com");
        doctor3.setTelefono("+57 555 123 4567");
        doctor3.setActivo(true);
        doctorRepository.save(doctor3);

        Doctor doctor4 = new Doctor();
        doctor4.setNombreCompleto("Dra. Ana Martínez");
        doctor4.setEspecialidad("Ginecología");
        doctor4.setEmail("ana.martinez@clinica.com");
        doctor4.setTelefono("+57 555 765 4321");
        doctor4.setActivo(true);
        doctorRepository.save(doctor4);

        Doctor doctor5 = new Doctor();
        doctor5.setNombreCompleto("Dr. Roberto Silva");
        doctor5.setEspecialidad("Ortopedia");
        doctor5.setEmail("roberto.silva@clinica.com");
        doctor5.setTelefono("+57 444 888 9999");
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

    private void crearUsuariosPrueba() {
        // Obtener todos los doctores recién creados
        List<Doctor> doctores = doctorRepository.findAll();
        
        if (doctores.size() >= 5) {
            // Crear usuarios para cada doctor usando los IDs reales
            crearUsuarioDoctor(doctores.get(0).getEmail(), "doctor123", "DOCTOR", doctores.get(0));
            crearUsuarioDoctor(doctores.get(1).getEmail(), "doctor123", "DOCTOR", doctores.get(1));
            crearUsuarioDoctor(doctores.get(2).getEmail(), "doctor123", "DOCTOR", doctores.get(2));
            crearUsuarioDoctor(doctores.get(3).getEmail(), "doctor123", "DOCTOR", doctores.get(3));
            crearUsuarioDoctor(doctores.get(4).getEmail(), "doctor123", "DOCTOR", doctores.get(4));
        }

        // Usuario Paciente
        Usuario pacienteUser = new Usuario();
        pacienteUser.setEmail("paciente@clinica.com");
        pacienteUser.setPassword(passwordEncoder.encode("paciente123"));
        pacienteUser.setRol("PACIENTE");
        pacienteUser.setActivo(true);
        usuarioRepository.save(pacienteUser);

        System.out.println(" Usuarios de prueba creados:");
        System.out.println("   Doctores: juan.perez@clinica.com, maria.garcia@clinica.com, etc. / doctor123");
        System.out.println("   Paciente: paciente@clinica.com / paciente123");
    }

    private void crearUsuarioDoctor(String email, String password, String rol, Doctor doctor) {
        Usuario usuarioDoctor = new Usuario();
        usuarioDoctor.setEmail(email);
        usuarioDoctor.setPassword(passwordEncoder.encode(password));
        usuarioDoctor.setRol(rol);
        usuarioDoctor.setDoctor(doctor); // Relacionar con el doctor
        usuarioDoctor.setActivo(true);
        usuarioRepository.save(usuarioDoctor);
    }
}