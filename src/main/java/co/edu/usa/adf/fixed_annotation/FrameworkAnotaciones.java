package co.edu.usa.adf.fixed_annotation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FrameworkAnotaciones {
	private Field[] field;
	private Annotation[] anotaciones;
	private Class clase;
	private String classname;
	private String ruta;

	public FrameworkAnotaciones(){
	}

	public void readDescriptor(String ruta) {

		try {
			String[] a = new String[2];
			String cadena;
			FileReader f = new FileReader(ruta);
			BufferedReader r = new BufferedReader(f);
			int pos = 0;
			while ((cadena = r.readLine()) != null) {

				a[pos] = cadena;
				pos++;
			}

			this.classname = a[0];
			this.ruta = a[1];

			r.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeTXT(Object obj) {
		try {
			clase = Class.forName(classname);
			field = clase.getDeclaredFields();
			anotaciones = clase.getAnnotations();
			FileWriter fw = new FileWriter(ruta, true);
			BufferedWriter bw = new BufferedWriter(fw);
			String valor = "";
			String write = "";
			for (Field fields : field) {
				anotaciones = fields.getAnnotations();
				for (Annotation anotacion : anotaciones) {

					if (fields.getType().getName().equals("java.util.Date")) {
						Method x = clase.getMethod("get" + mayus(fields.getName()));
						Date dat = (Date) x.invoke(obj);
						

						valor = dat+"";

						write += String.format("%1$-" + ((field) anotacion).width() + "s", valor);
					} else {
						Method x = clase.getMethod("get" + mayus(fields.getName()));
						valor = x.invoke(obj) + "";
						write += String.format("%1$-" + ((field) anotacion).width() + "s", valor);
					}
				}
			}
			bw.write(write + "\n");
			bw.close();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String mayus(String word) {
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}

	public ArrayList<Object> ReadTxt() {
		try {
			ArrayList<Object> rta = new ArrayList<Object>();
			clase = Class.forName(classname);

			field = clase.getDeclaredFields();
			anotaciones = clase.getAnnotations();
			String cadena;
			FileReader file = new FileReader(ruta);
			BufferedReader leer = new BufferedReader(file);
			while ((cadena = leer.readLine()) != null) {
				
				Object cls = clase.newInstance();
				int pos = 0;
				for (Field fields : field) {
					anotaciones = fields.getAnnotations();

					for (Annotation anotacion : anotaciones) {

						Method x = clase.getMethod("set" + mayus(fields.getName()), fields.getType());
						String data = cadena.substring(pos, pos + ((field) anotacion).width()).trim();
						
						if (fields.getType().getName().equals("java.lang.String")) {

							x.invoke(cls, data);
						} else if (fields.getType().getName().equals("int")) {
							x.invoke(cls, Integer.parseInt(data));
						} else if (fields.getType().getName().equals("java.lang.Double")) {
							
							x.invoke(cls, Double.parseDouble(data));
						} else if (fields.getType().getName().equals("boolean")) {
							x.invoke(cls, Boolean.parseBoolean(data));
						} else if (fields.getType().getName().equals("java.util.Date")) {

							String date = data;
							SimpleDateFormat dt = new SimpleDateFormat("yyyy/mm/dd");
							Date fec = dt.parse(date);

						
							x.invoke(cls, fec);
						}

						pos += ((field) anotacion).width();

					}
				}

				rta.add(cls);
			}

			leer.close();
			return rta;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("No se encontro la clase : " + e);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("No se encontro el archivo : " + e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
