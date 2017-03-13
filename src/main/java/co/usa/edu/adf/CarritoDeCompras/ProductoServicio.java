package co.usa.edu.adf.CarritoDeCompras;

import java.util.ArrayList;
import java.util.List;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import co.edu.usa.adf.fixed_annotation.FrameworkAnotaciones;

public class ProductoServicio {
	public static ProductoServicio instance;
	private ArrayList<Producto> producto = new ArrayList<>();
	public static ArrayList<Producto> productoAntiguo = new ArrayList<>();

	private ProductoServicio() {

	}

	public static ProductoServicio getInstance() {
		if (instance == null) {
			instance = new ProductoServicio();
			instance.llenarDatos();
		}
		return instance;
	}

	public synchronized List<Producto> findAll(String stringFilter) {
		ArrayList<Producto> arrayList = new ArrayList<>();
		for (Producto product : producto) {

			boolean passesFilter = (stringFilter == null || stringFilter.isEmpty())
					|| product.getNombre().toLowerCase().contains(stringFilter.toLowerCase());

			if (passesFilter) {

				arrayList.add(product);
			}

		}

		return arrayList;
	}

	public void save(ArrayList<Producto> a) {
		producto = a;
	}

	public void llenarDatos() {
		System.out.println("Inicializando ...");

		KieServices ks = KieServices.Factory.get();
		KieContainer kContainer = ks.getKieClasspathContainer();
		KieSession kSession = kContainer.newKieSession();

		FrameworkAnotaciones app = new FrameworkAnotaciones();

		app.readDescriptor("/home/bellyoz/Documentos/testTienda/descriptorProducto.txt");

		ArrayList<Object> lista = new ArrayList<>();
		ArrayList<Producto> producto = new ArrayList<>();
		lista = app.ReadTxt();

		llenarAntiguo();

		for (Object a : lista) {

			producto.add((Producto) a);

			kSession.insert(a);

		}

		int fired = kSession.fireAllRules();
		System.out.println("Reglas ejecutadas = " + fired);
		save(producto);
	}

	private void llenarAntiguo() {
		FrameworkAnotaciones app = new FrameworkAnotaciones();

		app.readDescriptor("/home/bellyoz/Documentos/testTienda/descriptorProducto.txt");
		for (Object producto2 : app.ReadTxt()) {
			productoAntiguo.add((Producto) producto2);
		}

	}
}
