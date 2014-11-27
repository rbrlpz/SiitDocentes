package mx.itdurango.rober.siitdocentes.estructuras;

import java.util.ArrayList;

/**
 * Created by rober on 21/11/14.
 */
public class AlumnosParciales {
    private String control;
    private String nombre;
    private String num;
    private ArrayList<String> calificaciones;

    public AlumnosParciales() {
        control = nombre = num = "";
        calificaciones = new ArrayList<String>();
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<String> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(ArrayList<String> calificaciones) {
        this.calificaciones = calificaciones;
    }
}
