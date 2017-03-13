package co.usa.edu.adf.CarritoDeCompras;

import co.edu.usa.adf.fixed_annotation.field;

public class Producto {
	@field(width = 25)
	private String rutaImg;

	@field(width = 15)
	private String nombre;

	@field(width = 6)
	private Double precio;
	
	@field(width = 10)
	private String tipo;
	

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Producto() {

	}

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

	public String getRutaImg() {
		return rutaImg;
	}

	public void setRutaImg(String rutaImg) {
		this.rutaImg = rutaImg;
	}

	@Override
	public String toString() {
		return "Producto [rutaImg=" + rutaImg + ", nombre=" + nombre + ", precio=" + precio + "]";
	}

}
