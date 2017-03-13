package co.usa.edu.adf.CarritoDeCompras;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.Action.Container;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.Position;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.CellReference;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.Renderer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.ValoTheme;

import co.edu.usa.adf.fixed_annotation.FrameworkAnotaciones;

@Theme("mytheme")
public class MyUI extends UI {
	private TextField filterText = new TextField();
	public static ProductoServicio servicio = ProductoServicio.getInstance();

	private Table tablita = new Table("Producto");
	private Button carrito = new Button(FontAwesome.SHOPPING_CART);
	private Button compra = new Button("Comprar");
	private Button nueva = new Button("Nueva Compra");
	private ArrayList<Producto> producto = new ArrayList<>();
	private ArrayList<Integer> cant = new ArrayList<>();
	private Table tabla = new Table("Carrito de Compras");
	private Button eliminar = new Button("X");
	private TextField total = new TextField();
	int pos = 0;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		tablita.setStyleName("tabla");
		total.setEnabled(false);
		tabla.setVisible(false);
		
		final VerticalLayout layout = new VerticalLayout();
		CssLayout filtro = new CssLayout();
		carrito.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		nueva.setStyleName(ValoTheme.BUTTON_PRIMARY);
		eliminar.setStyleName(ValoTheme.BUTTON_DANGER);
		compra.setStyleName(ValoTheme.BUTTON_FRIENDLY);
		filterText.setInputPrompt("ingresar el nombre");
		filtro.addComponent(filterText);
		tablita.addContainerProperty("Imagen", HorizontalLayout.class, null);
		tablita.addContainerProperty("Nombre", String.class, null);
		tablita.addContainerProperty("Precio Original", Double.class, null);
		tablita.addContainerProperty("Precio Actual", Double.class, null);
		tablita.addContainerProperty("Cantidad", TextField.class, null);

		tablita.setSelectable(true);

		filterText.addTextChangeListener(e -> {

			initTable(e);
		});
		tablita.setWidth("100%");
		tablita.setHeight("100%");
		tablita.setPageLength(0);
		tablita.setImmediate(true);
		
		carrito.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				tabla.setVisible(true);
			
				String nombre = (String) tablita.getContainerProperty(tablita.getValue(), "Nombre").getValue();
				Double price = (Double) tablita.getContainerProperty(tablita.getValue(), "Precio Actual").getValue();
				TextField cantidad = (TextField) tablita.getContainerProperty(tablita.getValue(), "Cantidad")
						.getValue();
				HorizontalLayout img = (HorizontalLayout) tablita.getContainerProperty(tablita.getValue(), "Imagen")
						.getValue();
				Image image = new Image();
				image = (Image) img.getComponent(0);

				Producto pro = new Producto();
				pro.setNombre(nombre);
				pro.setPrecio(price);
				pro.setRutaImg("" + image.getSource());
				producto.add(pro);
				cant.add(Integer.parseInt(cantidad.getValue()));

				tablaCarrito(producto, cant);
				total.setValue("Total : " + total());
				Notification notif = new Notification(
						"Agregado al carrito",
					    Notification.TYPE_HUMANIZED_MESSAGE);

				notif.setDelayMsec(1000);
				notif.setPosition(Position.TOP_CENTER);
				notif.show(Page.getCurrent());

			}
		});
		nueva.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Page.getCurrent().reload();
			}
		});
		eliminar.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				System.out.println("value " + tabla.getValue());
				int f = (int) tabla.getValue();
				producto.remove(f);
				cant.remove(f);
				tabla.removeAllItems();
				tablaCarrito(producto, cant);
				total.setValue("Total : " + total());

			}
		});
		compra.addClickListener(new Button.ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				// TODO Auto-generated method stub
				Notification notif = new Notification(
						"Compra realizada",
					    Notification.TYPE_WARNING_MESSAGE);

				notif.setDelayMsec(1000);
				notif.setPosition(Position.MIDDLE_CENTER);
				notif.show(Page.getCurrent());
				
				KieServices ks = KieServices.Factory.get();
			    KieContainer kContainer = ks.getKieClasspathContainer();
			    KieSession kSession =  kContainer.newKieSession();
			    
				
				FrameworkAnotaciones app = new FrameworkAnotaciones();
				app.readDescriptor("/home/bellyoz/Documentos/testTienda/descriptorCompra.txt");
				java.util.Date fecha = new Date();
				Double total = total();
				Compra compra = new Compra();

				ArrayList<Producto> lista = new ArrayList<>();
				// lista = compra.getProducto();
				for (int i = 0; i < producto.size(); i++) {
					compra.setFecha(fecha);
					compra.setNombre(producto.get(i).getNombre());
					compra.setPrecio(producto.get(i).getPrecio());
					compra.setTotal(total);
					
					kSession.insert(compra);
					System.out.println("total "+compra.getTotal());
					kSession.fireAllRules();
					app.writeTXT(compra);
					
				}
				
				

			}
		});
		HorizontalLayout horizontal = new HorizontalLayout(filtro, carrito);
		horizontal.setSpacing(true);
		horizontal.addComponent(nueva);
		CssLayout bottom = new CssLayout();
		bottom.addComponent(eliminar);

		HorizontalLayout bot = new HorizontalLayout(bottom, compra);
		bot.setSpacing(true);

		layout.addComponents(horizontal, tablita, bot, tabla, total);
		layout.setMargin(true);
		setContent(layout);

	}

	protected void tablaCarrito(ArrayList<Producto> producto2, ArrayList<Integer> cant2) {
		// TODO Auto-generated method stub
		tabla.addContainerProperty("Imagen", HorizontalLayout.class, null);
		tabla.addContainerProperty("Nombre", String.class, null);
		tabla.addContainerProperty("Precio", Double.class, null);
		tabla.addContainerProperty("Cantidad", Integer.class, null);

		tabla.setWidth("100%");
		tabla.setHeight("100%");
		tabla.setPageLength(0);
		tabla.setSelectable(true);
		tabla.setImmediate(true);
		tabla.removeAllItems();
		for (int i = 0; i < producto2.size(); i++) {
			HorizontalLayout img = new HorizontalLayout();
			ThemeResource resource = new ThemeResource(producto2.get(i).getRutaImg());
			Image image = new Image("", resource);
			image.setWidth("200px");
			image.setHeight("200px");

			img.addComponent(image);
			// System.out.println("llenar : " + producto2.get(i).getNombre() +
			// "cantidad " + cant2.get(i) + " id " + i);
			tabla.addItem(
					new Object[] { img, producto2.get(i).getNombre(), producto2.get(i).getPrecio(), cant2.get(i) }, i);

		}

	}

	public Double total() {
		Double sum = 0.0;
		for (int i = 0; i < cant.size(); i++) {
			sum += cant.get(i) * producto.get(i).getPrecio();
		}
		return sum;

	}

	public void initTable(TextChangeEvent e) {

		if (e.getText().length() == 0) {
			for (int i = 0; i <= pos; i++) {
				tablita.removeItem(i);
				// System.out.println("Borrando : " + i);
			}
		} else {
			ArrayList<Producto> lista = new ArrayList<>();
			lista = (ArrayList<Producto>) servicio.findAll(e.getText());

			for (Producto pro : lista) {
				// System.out.println("nombre "+pro.getNombre()+"poss "+pos);
				HorizontalLayout img = new HorizontalLayout();
				ThemeResource resource = new ThemeResource(pro.getRutaImg());
				Image image = new Image("", resource);
				image.setWidth("200px");
				image.setHeight("200px");
				img.addComponent(image);
				for (int i = 0; i <= (pos - lista.size()); i++) {
					tablita.removeItem(i);
					// System.out.println("Borrando : " + i);
				}
				tablita.setItemIcon(pos++, resource);

				tablita.addItem(new Object[] { img, pro.getNombre(), devolverPrecio(pro.getNombre()), pro.getPrecio(), new TextField() },
						new Integer(pos++));

			}

			// grid.setContainerDataSource(new
			// BeanItemContainer<>(Producto.class,
			// servicio.findAll(e.getText())));
		}
	}
	public double devolverPrecio(String a){
		for(Producto p : ProductoServicio.productoAntiguo){
			if(p.getNombre().equals(a)){
				return p.getPrecio();
			}
		}
		return 0.0;	
	}

	@WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
	@VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
	public static class MyUIServlet extends VaadinServlet {
	}
}
