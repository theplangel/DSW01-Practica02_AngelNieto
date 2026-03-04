package com.dsw.practica02.empleados.repository;

import com.dsw.practica02.empleados.domain.Empleado;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, UUID> {

    Optional<Empleado> findByClaveIgnoreCase(String clave);

    boolean existsByClaveIgnoreCase(String clave);

    List<Empleado> findAllByOrderByClaveAsc();
}
