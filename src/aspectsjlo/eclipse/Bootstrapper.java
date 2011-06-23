package aspectsjlo.eclipse;

import java.io.File;
import java.io.IOException;

import subobjectjava.input.JLoFactory;
import subobjectjava.output.JLoSyntax;
import aspectsjlo.build.AspectsBuilder;
import aspectsjlo.input.AspectsJLoModelFactory;
import aspectsjlo.model.language.AspectsJLo;
import chameleon.core.language.Language;
import chameleon.editor.connector.EclipseBootstrapper;
import chameleon.editor.connector.EclipseEditorExtension;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.input.ModelFactory;
import chameleon.input.ParseException;
import chameleon.oo.plugin.ObjectOrientedFactory;
import chameleon.plugin.build.Builder;
import chameleon.plugin.output.Syntax;

public class Bootstrapper extends EclipseBootstrapper {
	
	public final static String PLUGIN_ID="be.chameleon.eclipse.aspectsjlo";
	
	public void registerFileExtensions() {
//		addExtension("java"); This causes problems with the generated files after a refresh. Until
//		                      we have a source path, I will simply rename the API files to .jlow
		addExtension("aspect");
	}
	
	public String getLanguageName() {
		return "AspectsJLo";
	}

	public String getLanguageVersion() {
		return "1.0";
	}

	public String getVersion() {
		return "1.0";
	}

	public String getDescription() {
		return "JLo with AoP";
	}
	
	public String getLicense() {
		return "";
	}

	public Syntax getCodeWriter() {
		return null;
	}

	public Language createLanguage() throws IOException, ParseException {
		AspectsJLo result = new AspectsJLo();
		ModelFactory factory = new AspectsJLoModelFactory(result);
		factory.setLanguage(result, ModelFactory.class);
		try {
			loadAPIFiles(".aspect", PLUGIN_ID, factory);
		} catch(ChameleonProgrammerException exc) {
			// Object and String may not be present yet.
		}
		result.setPlugin(EclipseEditorExtension.class, new AspectsJLoEditorExtension());
		result.setPlugin(Syntax.class, new JLoSyntax());
		result.setPlugin(ObjectOrientedFactory.class, new JLoFactory());
		return result;
	}
	
	public Builder createBuilder(Language source, File projectDirectory) {
		File outputDirectory = new File(projectDirectory.getAbsolutePath()+File.separator+"java");
		return new AspectsBuilder((AspectsJLo) source, outputDirectory);

	}
}