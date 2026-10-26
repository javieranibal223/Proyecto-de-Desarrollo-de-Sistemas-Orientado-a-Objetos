package usecase;

import dao.IDao;
import model.Paciente;

public class GuardarPacienteCU {
    public Paciente ejecutar(Paciente paciente, IDao<Paciente> dao) {
        return dao.guardar(paciente);
    }
}