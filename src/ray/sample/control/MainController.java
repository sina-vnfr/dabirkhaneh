package ray.sample.control;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean(name = "MainController")
@ViewScoped
public class MainController {

	@PostConstruct
	public void init() {

		String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
		ExternalContext externalContextPages = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMapPages = externalContextPages.getSessionMap();
		sessionMapPages.put("previos", viewId);

	}

	public String writeLetter() {
		System.out.println("WriteLetter");
		return "writeLetter";
	}

	public String signature() {
		System.out.println("Signature");
		return "signature";
	}

	public String yourLetterStatus() {
		System.out.println("LetterStatus");
		return "letterStatus";
	}

	public String archiveLetter() {
		System.out.println("Archive");
		return "archive";
	}

	public String getBack() {
		return "login";
	}
}
