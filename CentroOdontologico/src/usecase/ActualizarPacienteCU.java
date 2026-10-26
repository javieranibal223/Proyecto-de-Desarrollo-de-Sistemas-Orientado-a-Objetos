package usecase;

import dao.IDao;
import model.Paciente;

public class ActualizarPacienteCU {
    public Paciente ejecutar(Paciente paciente, IDao<Paciente> dao) {
        return dao.actualizar(paciente);
    }
}