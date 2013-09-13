package cz.cuni.xrg.intlib.frontend.gui;

import com.vaadin.ui.Upload;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Wrapper for {@link Upload.SucceededListener} keeping reference to authentication
 * details, which were available when this wrapper was constructed.
 * 
 * <p>
 * It seems like {@link Upload.SucceededListener} is sometimes executed in
 * a dedicated thread, where authentication is lost. This wrapper should resolve
 * this situation by saving a local reference to {@link Authentication} while
 * it is constructed in authentication-aware thread. Then when event is
 * triggered, security context is filled with this authentication object.
 * For details about when such situation occurs see GH-415.
 *
 * @author Jan Vojt
 */
public class AuthAwareUploadSucceededWrapper implements Upload.SucceededListener {
	
	/**
	 * Wrapped listener.
	 */
	private Upload.SucceededListener succeededListener;
	
	/**
	 * Reference to authentication available during the construction of wrapper.
	 */
	private Authentication authentication;

	public AuthAwareUploadSucceededWrapper(Upload.SucceededListener succeededListener) {
		this.succeededListener = succeededListener;
		authentication = SecurityContextHolder.getContext().getAuthentication();
	}
	
	@Override
	public void uploadSucceeded(Upload.SucceededEvent event) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		succeededListener.uploadSucceeded(event);
	}

}