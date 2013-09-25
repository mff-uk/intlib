package cz.cuni.xrg.intlib.frontend.gui.views;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.Upload.FailedEvent;
import com.vaadin.ui.Upload.FailedListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Window.CloseListener;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.Validator;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;

import cz.cuni.xrg.intlib.commons.app.auth.IntlibPermissionEvaluator;
import cz.cuni.xrg.intlib.commons.app.dpu.DPUTemplateRecord;
import cz.cuni.xrg.intlib.commons.app.auth.VisibilityType;
import cz.cuni.xrg.intlib.commons.app.module.DPUReplaceException;
import cz.cuni.xrg.intlib.commons.app.module.DPUValidator;
import cz.cuni.xrg.intlib.commons.app.module.ModuleException;
import cz.cuni.xrg.intlib.commons.app.pipeline.Pipeline;
import cz.cuni.xrg.intlib.commons.configuration.DPUConfigObject;
import cz.cuni.xrg.intlib.commons.configuration.ConfigException;
import cz.cuni.xrg.intlib.commons.web.AbstractConfigDialog;
import cz.cuni.xrg.intlib.frontend.auxiliaries.App;
import cz.cuni.xrg.intlib.frontend.auxiliaries.MaxLengthValidator;
import cz.cuni.xrg.intlib.frontend.auxiliaries.dpu.DPUTemplateWrap;
import cz.cuni.xrg.intlib.frontend.gui.AuthAwareUploadSucceededWrapper;
import cz.cuni.xrg.intlib.frontend.gui.ViewComponent;
import cz.cuni.xrg.intlib.frontend.gui.ViewNames;
import cz.cuni.xrg.intlib.frontend.gui.components.DPUCreate;
import cz.cuni.xrg.intlib.frontend.gui.components.DPUTree;
import cz.cuni.xrg.intlib.frontend.gui.components.FileUploadReceiver;
import cz.cuni.xrg.intlib.frontend.gui.components.IntlibPagedTable;
import cz.cuni.xrg.intlib.frontend.gui.components.PipelineStatus;
import cz.cuni.xrg.intlib.frontend.gui.components.UploadInfoWindow;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.dialogs.ConfirmDialog;

/**
 * GUI for DPU Templates page which opens from the main menu. Contains DPU
 * Templates tree, DPU Details, actions buttons.
 *
 *
 * @author Maria Kukhar
 */
class DPU extends ViewComponent {

	private static final long serialVersionUID = 1L;
	private VerticalLayout mainLayout;
	private VerticalLayout verticalLayoutData; //Layout contains General tab components of {@link #tabSheet}.
	private VerticalLayout verticalLayoutConfigure;// Layout contains Template Configuration tab components of {@link #tabSheet}. 
	private VerticalLayout verticalLayoutInstances;//Layout contains DPU instances tab components of {@link #tabSheet}.  
	private VerticalLayout dpuDetailLayout; //Layout contains DPU Template details.
	private DPUTree dpuTree;// Tree contains available DPUs.
	private TextField dpuName; // name of selected DPU Template
	private TextArea dpuDescription; // description of selected DPU Template
	private Upload reloadFile; // button for reload JAR file
	private FileUploadReceiver fileUploadReceiver;
	public static UploadInfoWindow uploadInfoWindow;
	private boolean errorExtension = false;
	private Label jarPath;
	/**
	 * DPU Template details TabSheet contains General, Template Configuration,
	 * DPU instances tabs
	 */
	private TabSheet tabSheet;
	private OptionGroup groupVisibility; // Visibility of DPU Template: public or private
	private GridLayout dpuLayout; // Layout contains DPU Templates tree and DPU Template details.
	private HorizontalLayout buttonDpuBar; // Layout contains action buttons of DPU Template details.
	private HorizontalLayout layoutInfo; // Layout with the information that no DPU template was selected.
	/**
	 * Table with instances of DPU. Located on {@link #tabSheet} DPU instances
	 * tab.
	 */
	private IntlibPagedTable instancesTable;
	private IndexedContainer tableData; //container with instancesTable data
	private Long pipeId;
	// visible columns of instancesTable
	private static String[] visibleCols = new String[]{"id", "name", "description",
		"author", "actions"};
	// headers of columns in instancesTable
	private static String[] headers = new String[]{"Id", "Name", "Description",
		"Author", "Actions"};
	private DPUTemplateRecord selectedDpu = null;
	/**
	 * Evaluates permissions of currently logged in user.
	 */
	private IntlibPermissionEvaluator permissions = App.getApp().getBean(IntlibPermissionEvaluator.class);
	/**
	 * Wrap for selected DPUTemplateRecord.
	 */
	private DPUTemplateWrap selectedDpuWrap = null;
	
	private static final Logger LOG = LoggerFactory.getLogger(ViewComponent.class);

	private boolean saveAllow=false;
	int fl=0;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	/**
	 *
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public DPU() {
	}

	/**
	 * Layout contains DPU Templates page elements: buttons on the top: "Create
	 * DPU", "Import DPU", "Export All"; layout with DPU Templates tree
	 * {@link DPUTree} and DPU Template details
	 *
	 * @return mainLayout VerticalLayout with all components of DPU Templates
	 * page.
	 */
	@AutoGenerated
	private VerticalLayout buildMainLayout() {

		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setStyleName("mainLayout");


		// top-level component properties
		setWidth("100%");
		setHeight("100%");

		// Buttons on the top: "Create DPU", "Import DPU", "Export All"
		HorizontalLayout buttonBar = new HorizontalLayout();
		buttonBar.setSpacing(true);

		Button buttonCreateDPU = new Button();
		buttonCreateDPU.setCaption("Create DPU template");
		buttonCreateDPU.setHeight("25px");
		buttonCreateDPU.setWidth("150px");
		buttonCreateDPU
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//Open the dialog for DPU Template creation
				DPUCreate createDPU = new DPUCreate();
				App.getApp().addWindow(createDPU);
				createDPU.addCloseListener(new CloseListener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void windowClose(CloseEvent e) {
						//refresh DPU tree after closing DPU Template creation dialog 
						dpuTree.refresh();

					}
				});

			}
		});
		buttonBar.addComponent(buttonCreateDPU);


		Button buttonImportDPU = new Button();
		buttonImportDPU.setCaption("Import DPU template");
		buttonImportDPU.setHeight("25px");
		buttonImportDPU.setWidth("150px");
		buttonImportDPU.setEnabled(false);
		buttonImportDPU
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});
		buttonBar.addComponent(buttonImportDPU);

		Button buttonExportAll = new Button();
		buttonExportAll.setCaption("Export All");
		buttonExportAll.setHeight("25px");
		buttonExportAll.setWidth("150px");
		buttonExportAll.setEnabled(false);
		buttonExportAll
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});
		buttonBar.addComponent(buttonExportAll);

		mainLayout.addComponent(buttonBar);

		//layout with  DPURecord tree and DPURecord details 
		dpuLayout = buildDpuLayout();
		mainLayout.addComponent(dpuLayout);
		selectedDpu = null;
		return mainLayout;
	}

	/**
	 * Builds layout contains DPU Templates tree {@link DPUTree} and DPU
	 * Template details. Calls from {@link #buildMainLayout}
	 *
	 * @return dpuLayout GridLayout contains {@link DPUTree} and
	 * {@link #buildDPUDetailLayout}.
	 */
	private GridLayout buildDpuLayout() {

		dpuLayout = new GridLayout(3, 1);
		dpuLayout.setSpacing(true);
		dpuLayout.setHeight(630, Unit.PIXELS);
		dpuLayout.setRowExpandRatio(0, 0.01f);
		dpuLayout.setRowExpandRatio(1, 0.99f);

		// Layout with the information that no DPU template was selected.
		layoutInfo = new HorizontalLayout();
		layoutInfo.setHeight("100%");
		layoutInfo.setWidth("100%");
		Label infoLabel = new Label();
		infoLabel.setImmediate(false);
		infoLabel.setWidth("-1px");
		infoLabel.setHeight("-1px");
		infoLabel.setValue("<br><br><br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Select DPU template from the DPU template tree for displaying it's details.");
		infoLabel.setContentMode(ContentMode.HTML);
		layoutInfo.addComponent(infoLabel);

		//DPU Template Tree
		dpuTree = new DPUTree(false);
		dpuTree.addItemClickListener(new ItemClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void itemClick(final ItemClickEvent event) {
				//if the previous selected
				if ((selectedDpu != null) && (selectedDpu.getId() != null) 
						&&(isChanged()) &&(saveAllow)) {
						
					//open confirmation dialog
					ConfirmDialog.show(UI.getCurrent(), "Unsaved changes",
							"There are unsaved changes.\nDo you wish to save them or discard?",
							"Save", "Discard changes",
							new ConfirmDialog.Listener() {
						private static final long serialVersionUID = 1L;

						@Override
						public void onClose(ConfirmDialog cd) {
							if (cd.isConfirmed()) {
			
								saveDPUTemplate();
								selectNewDPU(event);
								dpuTree.refresh();
							} 
							else{
								selectNewDPU(event);
							}
						}
					});

				}
				else
					selectNewDPU(event);


			}

		});


		dpuLayout.addComponent(dpuTree, 0, 0);
		dpuLayout.addComponent(layoutInfo, 2, 0);

		return dpuLayout;
	}
	
	private void selectNewDPU(DPUTemplateRecord dpu) {
		selectedDpu = dpu;
		saveAllow = permissions.hasPermission(selectedDpu, "save");


		//If DPURecord that != null was selected then it's details will be shown.
		if ((selectedDpu != null) && (selectedDpu.getId() != null)) {
			// crate new wrap
			selectedDpuWrap = new DPUTemplateWrap(selectedDpu);

			dpuLayout.removeComponent(dpuDetailLayout);
			dpuLayout.removeComponent(layoutInfo);
			dpuDetailLayout = buildDPUDetailLayout();
			dpuLayout.addComponent(dpuDetailLayout, 1, 0);

			// show/hide replace button
			reloadFile.setVisible(
					selectedDpuWrap.getDPUTemplateRecord().jarFileReplacable());
			
			setGeneralTabValues();
			//Otherwise, the information layout will be shown.
		} else {
			dpuLayout.removeComponent(dpuDetailLayout);
			dpuLayout.removeComponent(layoutInfo);
			dpuLayout.addComponent(layoutInfo, 2, 0);

		}
	}
	
	private void selectNewDPU(ItemClickEvent event){

		//If the first level of the DPU tree (category Extractors, Transformer, Loaders)
		//was selected then information layout will be shown.
		if (event.getItemId().getClass() != DPUTemplateRecord.class) {
			dpuLayout.removeComponent(dpuDetailLayout);
			dpuLayout.removeComponent(layoutInfo);
			dpuLayout.addComponent(layoutInfo, 2, 0);
			return;
		}

		selectNewDPU((DPUTemplateRecord) event.getItemId());
	}

	/**
	 * Builds layout with DPU Template details of DPU selected in the tree. DPU
	 * Template details represents by {@link #tabSheet}. Calls from
	 * {@link #buildDpuLayout}
	 *
	 * @return dpuDetailLayout VerticalLayout with {@link #tabSheet} that
	 * contain all DPU Template details components.
	 */
	private VerticalLayout buildDPUDetailLayout() {

		dpuDetailLayout = new VerticalLayout();
		dpuDetailLayout.setImmediate(true);
		dpuDetailLayout.setStyleName("dpuDetailLayout");
		dpuDetailLayout.setMargin(true);

		//DPU Details TabSheet
		tabSheet = new TabSheet();
		tabSheet.setWidth(630, Unit.PIXELS);
		tabSheet.setHeight(350, Unit.PIXELS);


		//General tab. Contains informations: name, description, visibility,
		//information about JAR file.
		verticalLayoutData = buildVerticalLayoutData();
		Tab dataTab = tabSheet.addTab(verticalLayoutData, "General");

		//Template Configuration tab. Contains information about configuration 
		//from JAR file
		verticalLayoutConfigure = new VerticalLayout();
		verticalLayoutConfigure.setImmediate(false);
		verticalLayoutConfigure.setMargin(true);
		tabSheet.addTab(verticalLayoutConfigure, "Template Configuration");
		tabSheet.setSelectedTab(dataTab);

		if (selectedDpuWrap != null) {
			AbstractConfigDialog<DPUConfigObject> configDialog = null;
			//getting configuration dialog of selected DPU Template
			try {
				configDialog = selectedDpuWrap.getDialog();
			} catch (ModuleException ex) {
				Notification.show(
						"Failed to load configuration dialog",
						ex.getMessage(), Type.ERROR_MESSAGE);
				LOG.error("Can't load DPU '{}'", selectedDpuWrap.getDPUTemplateRecord().getId(), ex);
			} catch (FileNotFoundException ex) {
				Notification.show(
						"File not found",
						ex.getMessage(), Type.ERROR_MESSAGE);
				LOG.error("Can't load DPU '{}'", selectedDpuWrap.getDPUTemplateRecord().getId(), ex);
			} catch (Exception ex) {
				Notification.show("Failed to load configuration dialog", ex.getMessage(), Type.ERROR_MESSAGE);
				LOG.error("Can't load DPU '{}'", selectedDpuWrap.getDPUTemplateRecord().getId(), ex);
			}

			verticalLayoutConfigure.removeAllComponents();
			if (configDialog == null) {
				// use some .. dummy component
			} else {
				// configure
				try {
					selectedDpuWrap.configuredDialog();
				} catch (ConfigException e) {
					Notification.show(
							"Failed to load configuration. The dialog defaul configuration is used.",
							e.getMessage(), Type.WARNING_MESSAGE);
					LOG.error("Failed to load configuration for {}", selectedDpuWrap.getDPUTemplateRecord().getId(), e);
				}
				// add dialog
//				configDialog.setEnabled(permissions.hasPermission(selectedDpu, "save"));
				
				verticalLayoutConfigure.addComponent(configDialog);

			}
		}

		//DPU instances tab. Contains pipelines using the given DPU. 
		verticalLayoutInstances = buildVerticalLayoutInstances();
		tabSheet.addTab(verticalLayoutInstances, "DPU instances");

		dpuDetailLayout.addComponent(tabSheet);
		buttonDpuBar = buildDPUButtonBur();
		dpuDetailLayout.addComponent(buttonDpuBar);

		return dpuDetailLayout;
	}

	/**
	 * Builds layout contains General tab of {@link #tabSheet}. Calls from
	 * {@link #buildDPUDetailLayout}
	 *
	 * @return verticalLayoutData VerticalLayout with all components of General
	 * tab.
	 */
	private VerticalLayout buildVerticalLayoutData() {

		// common part: create layout
		verticalLayoutData = new VerticalLayout();
		verticalLayoutData.setImmediate(false);
		verticalLayoutData.setWidth("100.0%");
		verticalLayoutData.setHeight("100%");
		verticalLayoutData.setMargin(true);

		//Layout contains name description and visibility of DPU Template
		GridLayout dpuSettingsLayout = new GridLayout(2, 5);
		dpuSettingsLayout.setStyleName("dpuSettingsLayout");
		dpuSettingsLayout.setMargin(true);
		dpuSettingsLayout.setSpacing(true);
		dpuSettingsLayout.setWidth("100%");
		dpuSettingsLayout.setHeight("100%");
		dpuSettingsLayout.setColumnExpandRatio(0, 0.10f);
		dpuSettingsLayout.setColumnExpandRatio(1, 0.90f);

		//Name of DPU Template: label & TextField
		Label nameLabel = new Label("Name:");
		nameLabel.setImmediate(false);
		nameLabel.setWidth("-1px");
		nameLabel.setHeight("-1px");
		dpuSettingsLayout.addComponent(nameLabel, 0, 0);
		dpuName = new TextField();
		dpuName.setImmediate(true);
		dpuName.setWidth("200px");
		dpuName.setHeight("-1px");
		//settings of mandatory
		dpuName.addValidator(new Validator() {
			private static final long serialVersionUID = 1L;

			@Override
			public void validate(Object value) throws InvalidValueException {
				if (value.getClass() == String.class
						&& !((String) value).isEmpty()) {
					return;
				}
				throw new InvalidValueException("Name must be filled!");
			}
		});
		dpuName.addValidator(new MaxLengthValidator(MaxLengthValidator.DPU_NAME_LENGTH));
		dpuSettingsLayout.addComponent(dpuName, 1, 0);

		//Description of DPU Template: label & TextArea
		Label descriptionLabel = new Label("Description:");
		descriptionLabel.setImmediate(false);
		descriptionLabel.setWidth("-1px");
		descriptionLabel.setHeight("-1px");
		dpuSettingsLayout.addComponent(descriptionLabel, 0, 1);
		dpuDescription = new TextArea();
		dpuDescription.addValidator(new MaxLengthValidator(MaxLengthValidator.DESCRIPTION_LENGTH));
		dpuDescription.setImmediate(true);
		dpuDescription.setWidth("100%");
		dpuDescription.setHeight("60px");
		dpuSettingsLayout.addComponent(dpuDescription, 1, 1);

		//Visibility of DPU Template: label & OptionGroup
		Label visibilityLabel = new Label("Visibility:");
		dpuSettingsLayout.addComponent(visibilityLabel, 0, 2);
		groupVisibility = new OptionGroup();
		groupVisibility.addStyleName("horizontalgroup");
		groupVisibility.addItem(VisibilityType.PRIVATE);
		groupVisibility.addItem(VisibilityType.PUBLIC);
		dpuSettingsLayout.addComponent(groupVisibility, 1, 2);

		// JAR path of DPU Template.
		HorizontalLayout jarPathLayout = new HorizontalLayout();
		jarPathLayout.setImmediate(false);
		jarPathLayout.setSpacing(true);
		jarPathLayout.setHeight("100%");
		dpuSettingsLayout.addComponent(new Label("JAR path:"), 0, 3);
		jarPath = new Label(selectedDpuWrap.getDPUTemplateRecord().getJarPath());


		//reload JAR file button
		fileUploadReceiver = new FileUploadReceiver();
		reloadFile = new Upload(null, fileUploadReceiver);
		reloadFile.setImmediate(true);
		reloadFile.setButtonCaption("Replace");
		reloadFile.addStyleName("horizontalgroup");
		reloadFile.setHeight("40px");
		reloadFile.setEnabled(permissions.hasPermission(selectedDpu, "save"));
		
		reloadFile.addStartedListener(new StartedListener() {
			/**
			 * Upload start listener. If selected file has JAR extension then an
			 * upload status window with upload progress bar will be shown. If
			 * selected file has other extension, then upload will be
			 * interrupted and error notification will be shown.
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadStarted(final StartedEvent event) {
				String filename = event.getFilename();
				String extension = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
				String jar = "jar";

				if (!jar.equals(extension)) {
					reloadFile.interruptUpload();
					errorExtension = true;
					Notification.show(
							"Selected file is not .jar file", Notification.Type.ERROR_MESSAGE);

					return;

				}

				if (uploadInfoWindow.getParent() == null) {
					UI.getCurrent().addWindow(uploadInfoWindow);
				}
				uploadInfoWindow.setClosable(false);


			}
		});


		reloadFile.addSucceededListener(new AuthAwareUploadSucceededWrapper(new SucceededListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadSucceeded(SucceededEvent event) {
				uploadInfoWindow.close();
				if (!errorExtension) {
					copyToTarget();
				} else {
					errorExtension = false;
				}
				DPUTemplateRecord dpu = App.getDPUs().getTemplate(selectedDpu.getId());
				selectNewDPU(dpu);
			}
		}));


		reloadFile.addFailedListener(new FailedListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void uploadFailed(FailedEvent event) {
				uploadInfoWindow.close();
				if (errorExtension) {
					errorExtension = false;
				}

				Notification.show(
						"Uploading "
						+ event.getFilename() + " failed.", Notification.Type.ERROR_MESSAGE);

			}
		});

		// Upload status window
		uploadInfoWindow = new UploadInfoWindow(reloadFile);

		jarPathLayout.addComponent(jarPath);
		jarPathLayout.addComponent(reloadFile);
		dpuSettingsLayout.addComponent(jarPathLayout, 1, 3);

		// Description of JAR of DPU Template.
		dpuSettingsLayout.addComponent(new Label("Description of JAR:"), 0, 4);
		TextArea jDescription = new TextArea(selectedDpuWrap.getDPUTemplateRecord().getJarDescription());
		jDescription.setReadOnly(true);
		jDescription.setWidth("100%");
		jDescription.setHeight("100%");
		dpuSettingsLayout.addComponent(jDescription, 1, 4);

		verticalLayoutData.addComponent(dpuSettingsLayout);

		return verticalLayoutData;
	}

	/**
	 * Reload DPU. The new DPU's jar file is accessible through the 
	 * {@link FileUploadReceiver#path}. The current DPU, which is being replaced
	 * , is assumed to be stored in {@link selectedDpu}.
	 */
	private void copyToTarget() {
		if (fileUploadReceiver.path == null) {
			// we have no file, end 
			return;
		}
		
		// prepare dpu validators
		List<DPUValidator> validators = new LinkedList<>();
		
		try {
			App.getApp().getDPUManipulator().replace(
					selectedDpuWrap.getDPUTemplateRecord(), 
					fileUploadReceiver.file,
					validators);
		} catch (DPUReplaceException e) {
			Notification.show("Failed to replace DPU",
					e.getMessage(), Notification.Type.ERROR_MESSAGE);
			return;
		}
		
		// we are ending .. refresh data in dialog 
		setGeneralTabValues();
		
		// and show message to the user that the replace has been successful
		Notification.show("Replace finished", Notification.Type.HUMANIZED_MESSAGE);		
	}

	/**
	 * Builds layout contains DPU instances tab of {@link #tabSheet}. Calls from
	 * {@link #buildDPUDetailLayout}
	 *
	 * @return verticalLayoutInstances VerticalLayout with all components of DPU
	 * instances tab.
	 */
	private VerticalLayout buildVerticalLayoutInstances() {

		// common part: create layout
		verticalLayoutInstances = new VerticalLayout();
		verticalLayoutInstances.setImmediate(false);
		verticalLayoutInstances.setWidth("100.0%");
		verticalLayoutInstances.setMargin(true);

		tableData = getTableData();

		//Table with instancesof DPU
		instancesTable = new IntlibPagedTable();
		instancesTable.setSelectable(true);
		instancesTable.setCaption("Pipelines:");
		instancesTable.setContainerDataSource(tableData);

		instancesTable.setWidth("100%");
		instancesTable.setImmediate(true);
		instancesTable.setVisibleColumns((Object[]) visibleCols);
		instancesTable.setColumnHeaders(headers);

		instancesTable.addGeneratedColumn("actions",
				new actionColumnGenerator());

		verticalLayoutInstances.addComponent(instancesTable);
		verticalLayoutInstances.addComponent(instancesTable.createControls());
		instancesTable.setFilterFieldVisible("actions", false);
		instancesTable.setPageLength(6);

		return verticalLayoutInstances;
	}

	/**
	 * Building layout contains action buttons of DPU Template details. Copy,
	 * Delete, Export, Save.
	 *
	 * @return buttonDpuBar HorizontalLayout contains action buttons.
	 */
	private HorizontalLayout buildDPUButtonBur() {

		buttonDpuBar = new HorizontalLayout();
		buttonDpuBar.setWidth("100%");
		buttonDpuBar.setHeight(30, Unit.PIXELS);
		buttonDpuBar.setSpacing(false);

		// Copy DPU Template Button, may copy only DPU of 3 level.
		Button buttonCopyDPU = new Button();
		buttonCopyDPU.setCaption("Copy");
		buttonCopyDPU.setHeight("25px");
		buttonCopyDPU.setWidth("100px");
		buttonCopyDPU.setEnabled(permissions.hasPermission(selectedDpu, "copy"));
		if (selectedDpu.getParent() != null) {
			buttonCopyDPU.setEnabled(true);
		}
		buttonCopyDPU
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				int i = 1;
				boolean found = true;
				String nameOfDpuCopy = "";
				List<DPUTemplateRecord> allDpus = App.getApp().getDPUs().getAllTemplates();
				while (found) {
					found = false;
					nameOfDpuCopy = "Copy of " + selectedDpu.getName();
					if (i > 1) {
						nameOfDpuCopy = nameOfDpuCopy + " " + i;
					}

					for (DPUTemplateRecord dpu : allDpus) {
						if (dpu.getName().equals(nameOfDpuCopy)) {
							found = true;
							break;
						}
					}
					i++;
				}

				DPUTemplateRecord copyDpuTemplate = new DPUTemplateRecord(selectedDpu);
				copyDpuTemplate.setName(nameOfDpuCopy);
				copyDpuTemplate.setParent(selectedDpu.getParent());
				App.getApp().getDPUs().save(copyDpuTemplate);

				//refresh data in dpu tree
				dpuTree.refresh();

			}
		});
		buttonDpuBar.addComponent(buttonCopyDPU);
		buttonDpuBar.setExpandRatio(buttonCopyDPU, 0.85f);
		buttonDpuBar
				.setComponentAlignment(buttonCopyDPU, Alignment.BOTTOM_LEFT);

		// Delete DPU Template Button
		Button buttonDeleteDPU = new Button();
		buttonDeleteDPU.setCaption("Delete");
		buttonDeleteDPU.setHeight("25px");
		buttonDeleteDPU.setWidth("100px");
		buttonDeleteDPU.setEnabled(permissions.hasPermission(selectedDpu, "delete"));
		buttonDeleteDPU
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				//open confirmation dialog
				ConfirmDialog.show(UI.getCurrent(),"Confirmation of deleting DPU template",
						"Delete " + selectedDpu.getName().toString() + " DPU template?","Delete", "Cancel",
						new ConfirmDialog.Listener() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClose(ConfirmDialog cd) {
						if (cd.isConfirmed()) {
							deleteDPU();

						}
					}
				});

			}
		});
		buttonDpuBar.addComponent(buttonDeleteDPU);
		buttonDpuBar.setExpandRatio(buttonDeleteDPU, 0.85f);
		buttonDpuBar.setComponentAlignment(buttonDeleteDPU,
				Alignment.BOTTOM_LEFT);

		// Export DPU Template Button
		Button buttonExportDPU = new Button();
		buttonExportDPU.setCaption("Export");
		buttonExportDPU.setHeight("25px");
		buttonExportDPU.setWidth("100px");
		buttonExportDPU.setEnabled(permissions.hasPermission(selectedDpu, "export"));
		buttonExportDPU
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				
			}
		});
		buttonDpuBar.addComponent(buttonExportDPU);
		buttonDpuBar.setExpandRatio(buttonExportDPU, 2.55f);
		buttonDpuBar.setComponentAlignment(buttonExportDPU,
				Alignment.BOTTOM_LEFT);

		// Save DPU Template Button
		Button buttonSaveDPU = new Button();
		buttonSaveDPU.setCaption("Save");
		buttonSaveDPU.setHeight("25px");
		buttonSaveDPU.setWidth("100px");
		buttonSaveDPU.setEnabled(permissions.hasPermission(selectedDpu, "save"));
		buttonSaveDPU
				.addClickListener(new com.vaadin.ui.Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				
				saveDPUTemplate();
				
				//refresh data in dialog and dpu tree
				dpuTree.refresh();
				setGeneralTabValues();

				// refresh configuration
				try {
					selectedDpuWrap.configuredDialog();
				} catch (ConfigException e) {
					Notification.show(
							"Failed to load configuration. The dialog defaul configuration is used.",
							e.getMessage(), Type.WARNING_MESSAGE);
					LOG.error("Failed to load configuration for {}", selectedDpuWrap.getDPUTemplateRecord().getId(), e);
				}
			}
		});
		buttonDpuBar.addComponent(buttonSaveDPU);
		buttonDpuBar.setComponentAlignment(buttonSaveDPU,
				Alignment.BOTTOM_RIGHT);
		dpuDetailLayout.addComponent(buttonDpuBar);

		return buttonDpuBar;
	}
	
	private boolean validate() {
		try {
			dpuName.validate();
			dpuDescription.validate();
		} catch (Validator.InvalidValueException e) {
			Notification.show("Error validating DPU", e.getMessage(), Notification.Type.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	/**
	 * Store DPU Template record to DB
	 */
	private void saveDPUTemplate(){

		//control of the validity of Name field.
		if (!validate()) {
			//Notification.show("Failed to save DPURecord", "Mandatory fields should be filled", Notification.Type.ERROR_MESSAGE);
			return;
		}
		//saving Name, Description and Visibility
		if ((selectedDpuWrap != null)
				&& (selectedDpuWrap.getDPUTemplateRecord().getId() != null)) {
			selectedDpuWrap.getDPUTemplateRecord().setName(dpuName.getValue().trim());
			selectedDpuWrap.getDPUTemplateRecord().setDescription(dpuDescription
					.getValue().trim());
			selectedDpuWrap.getDPUTemplateRecord()
					.setVisibility((VisibilityType) groupVisibility
					.getValue());

			// saving configuration
			try {
				selectedDpuWrap.saveConfig();
			} catch (ConfigException e) {
				selectedDpuWrap.getDPUTemplateRecord().setRawConf(null);
			}

			// store into DB
			App.getDPUs().save(selectedDpuWrap.getDPUTemplateRecord());
			Notification.show("DPURecord was saved",
					Notification.Type.HUMANIZED_MESSAGE);


		}
	
	}

	/**
	 * Set values to components
	 * {@link #dpuName}, {@link #dpuDescription}, {@link #groupVisibility}
	 */
	public void setGeneralTabValues() {

		String selectedDpuName = selectedDpuWrap
				.getDPUTemplateRecord().getName();
		String selecteDpuDescription = selectedDpuWrap
				.getDPUTemplateRecord().getDescription();
		VisibilityType selecteDpuVisibility = selectedDpuWrap
				.getDPUTemplateRecord().getVisibility();
		dpuName.setValue(selectedDpuName.trim());
		dpuName.setReadOnly(!permissions.hasPermission(selectedDpu, "save"));
		dpuDescription.setValue(selecteDpuDescription.trim());
		dpuDescription.setReadOnly(!permissions.hasPermission(selectedDpu, "save"));

		groupVisibility.setValue(selecteDpuVisibility);
		groupVisibility.setEnabled(true);
		if (selecteDpuVisibility == VisibilityType.PUBLIC) {
			groupVisibility.setValue(selecteDpuVisibility);
			groupVisibility.setEnabled(false);
		} else {
			groupVisibility.setValue(selecteDpuVisibility);
			groupVisibility.setEnabled(true);
		}
	}

	/**
	 * Return container with data that used in {@link #instancesTable}.
	 *
	 * @return result IndexedContainer for {@link #instancesTable}
	 */
	@SuppressWarnings("unchecked")
	public IndexedContainer getTableData() {

		IndexedContainer result = new IndexedContainer();

		for (String p : visibleCols) {
			// setting type of the columns
			if (p.equals("id")) {
				result.addContainerProperty(p, Long.class, null);
			} else {
				result.addContainerProperty(p, String.class, "");
			}

		}
		// getting all Pipelines with specified DPU in it
		List<Pipeline> pipelines = App.getPipelines().getPipelinesUsingDPU(selectedDpu);
		for (Pipeline pitem : pipelines) {

			Object num = result.addItem();

			result.getContainerProperty(num, "id").setValue(
					pitem.getId());
			result.getContainerProperty(num, "name").setValue(
					pitem.getName());
			result.getContainerProperty(num, "description")
					.setValue(pitem.getDescription());
			result.getContainerProperty(num, "author")
					.setValue("");
		}

		return result;
	}

	/**
	 * Delete DPU Template if it's unused by any pipeline
	 */
	public void deleteDPU() {
		List<Pipeline> pipelines = App.getPipelines().getPipelinesUsingDPU(selectedDpu);

		//If DPU Template is unused by any pipeline
		if (pipelines.isEmpty()) {
			//find if DPU Template has child elements
			List<DPUTemplateRecord> childDpus = App.getApp().getDPUs().getChildDPUs(selectedDpu);
			if (!childDpus.isEmpty()) {
				Notification.show("DPURecord can not be removed because it has child elements", Notification.Type.ERROR_MESSAGE);
				return;
			}

			//if DPU Template hasn't child elements then delete it.
			if (selectedDpu.getParent() == null) {
				// first level DPU .. delete it completely
				App.getApp().getDPUManipulator().delete(selectedDpuWrap.getDPUTemplateRecord());
			} else {
				// 2+ level DPU .. just delete the database record
				App.getApp().getDPUs()
				.delete(selectedDpuWrap.getDPUTemplateRecord());
			}
			// and refresh the layout
			dpuTree.refresh();
			dpuDetailLayout.removeAllComponents();			
			
			Notification.show("DPURecord was removed",
					Notification.Type.HUMANIZED_MESSAGE);
		} //If DPU Template it used by any pipeline, than show the names of this pipelines.
		else if (pipelines.size() == 1) {
			Notification.show("DPURecord can not be removed because it has been used in Pipeline: ", pipelines.get(0).getName(), Notification.Type.WARNING_MESSAGE);
		} else {
			Iterator<Pipeline> iterator = pipelines.iterator();
			StringBuilder names = new StringBuilder(iterator.next().getName());
			while (iterator.hasNext()) {
				names.append(", ");
				names.append(iterator.next().getName());
			}
			names.append('.');
			Notification.show("DPURecord can not be removed because it has been used in Pipelines: ", names.toString(), Notification.Type.WARNING_MESSAGE);
		}
	}

	@Override
	public void enter(ViewChangeEvent event) {
		buildMainLayout();
		setCompositionRoot(mainLayout);
	}

	@Override
	public boolean isModified() {
		return ((selectedDpu != null) && (selectedDpu.getId() != null)
				&& (isChanged()) && (saveAllow));
	}
	
	@Override
	public boolean saveChanges() {		
		//control of the validity of Name field.
		if (!validate()) {
			//Notification.show("Failed to save DPURecord", "Mandatory fields should be filled", Notification.Type.ERROR_MESSAGE);
			return false;
		}
		saveDPUTemplate();
		return true;
	}
	
	public boolean isChanged() {
		
		if (!dpuName.getValue().equals(selectedDpu.getName()))
			return true;
		else if (!dpuDescription.getValue().equals(selectedDpu.getDescription()))
			return true;
		else if (!groupVisibility.getValue().equals(selectedDpu.getVisibility()))
			return true;
		else if (!jarPath.getValue().equals(selectedDpu.getJarPath()))
			return true;
		else
		return false;
	}

	/**
	 * Generate column in table {@link #instancesTable}. with buttons:Detail,
	 * Delete, Status.
	 *
	 * @author Maria Kukhar
	 *
	 */
	class actionColumnGenerator implements
			com.vaadin.ui.CustomTable.ColumnGenerator {

		private static final long serialVersionUID = 1L;

		@Override
		public Object generateCell(final CustomTable source,
				final Object itemId, Object columnId) {



			HorizontalLayout layout = new HorizontalLayout();
			//Detail button
			Button detailButton = new Button();
			detailButton.setCaption("Detail");
			detailButton.setWidth("70px");
			detailButton
					.addClickListener(new com.vaadin.ui.Button.ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {

					pipeId = (Long) tableData
							.getContainerProperty(itemId, "id").getValue();

					// navigate to PIPELINE_EDIT
					App.getApp()
							.getNavigator()
							.navigateTo(
							ViewNames.PIPELINE_EDIT.getUrl()
							+ "/" + pipeId.toString());

				}
			});
			layout.addComponent(detailButton);


			//Delete button. Delete pipeline.
			Button deleteButton = new Button();
			deleteButton.setCaption("Delete");
			deleteButton.setWidth("70px");
			deleteButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					pipeId = (Long) tableData
							.getContainerProperty(itemId, "id").getValue();
					List<Pipeline> pipelines = App.getApp().getPipelines()
							.getAllPipelines();
					for (Pipeline item : pipelines) {
						if (item.getId().equals(pipeId)) {
							// navigate to PIPELINE_EDIT/New
							App.getApp().getPipelines().delete(item);
							break;
						}
					}

					// now we have to remove pipeline from table
					source.removeItem(itemId);

				}
			});
			layout.addComponent(deleteButton);

			//Status button
			Button statusButton = new Button();
			statusButton.setCaption("Status");
			statusButton.setWidth("70px");
			statusButton.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					PipelineStatus pipelineStatus = new PipelineStatus();

					pipeId = (Long) tableData
							.getContainerProperty(itemId, "id").getValue();
					List<Pipeline> pipelines = App.getApp().getPipelines()
							.getAllPipelines();
					for (Pipeline item : pipelines) {
						if (item.getId().equals(pipeId)) {
							pipelineStatus.setSelectedPipeline(item);
							break;
						}
					}
					// open the window with status parameters.
					App.getApp().addWindow(pipelineStatus);

				}
			});
			layout.addComponent(statusButton);

			return layout;
		}
	}
}
