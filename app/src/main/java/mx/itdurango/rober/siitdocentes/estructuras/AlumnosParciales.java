/**
 Copyright (c) <2014> José Roberto López Quiñones <rlopez@itdurango.edu.mx>

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */
/**
 * Autor: Roberto Lopez
 * Fecha: 21/11/2014
 */
package mx.itdurango.rober.siitdocentes.estructuras;

import java.util.ArrayList;

/**
 * Estructura para almacenar los datos de los alumnos y calificaciones
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
