package co.usa.edu.adf.CarritoDeCompras;

import java.util.Date;

import co.edu.usa.adf.fixed_annotation.field;

import java.util.ArrayList;

public class Compra {
	@field(width=20)private Date fecha;
	@field(width=15)private String nombre;
	@field(width=8) private Double precio;
	@field(width=10)private Double total;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Double getPrecio() {
		return precio;
	}
	public void setPrecio(Double precio) {
		this.precio = precio;
	}
	
	
	
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	
}
