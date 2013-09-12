package cz.cuni.xrg.intlib.frontend.gui.views;

import java.text.DateFormat;
import java.sql.Timestamp;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Container;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

import static cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus.CANCELLED;
import static cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus.FAILED;
import static cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus.FINISHED_SUCCESS;
import static cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus.FINISHED_WARNING;
import static cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus.RUNNING;
import static cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus.SCHEDULED;

import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecutionStatus;
import cz.cuni.xrg.intlib.commons.app.pipeline.PipelineExecution;
import cz.cuni.xrg.intlib.frontend.auxiliaries.App;
import cz.cuni.xrg.intlib.frontend.auxiliaries.IntlibHelper;
import cz.cuni.xrg.intlib.frontend.container.IntlibLazyQueryContainer;
import cz.cuni.xrg.intlib.frontend.gui.ViewComponent;
import cz.cuni.xrg.intlib.frontend.gui.components.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GUI for Execution Monitor page which opens from the main menu. Contains table
 * with pipeline execution records. Is the opportunity to open
 * {@link DebuggingView} from the table
 *
 * @author Maria Kukhar
 * @author Bogo
 */
public class ExecutionMonitor extends ViewComponent implements ClickListener {

    @AutoGenerated
    private VerticalLayout monitorTableLayout;
    private VerticalLayout logLayout;
    private HorizontalSplitPanel hsplit;
    private Panel mainLayout;
	
	private static final Logger LOG = LoggerFactory.getLogger(ExecutionMonitor.class);

    /**
     * Table contains pipeline executions.
     */
    private IntlibPagedTable monitorTable;
    private Container tableData;
    private Long exeId;
    int style = DateFormat.MEDIUM;
    static String[] visibleCols = new String[]{"start", "pipeline.name", "duration", "user",
        "status", "isDebugging", "obsolete", "actions", "report"};
    static String[] headers = new String[]{"Date", "Name", "Run time", "User", "Status",
        "Debug", "Obsolete", "Actions", "Report"};

    /*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
    /**
     * The constructor should first build the main layout, set the composition
     * root and then do any custom initialization.
     *
     * The constructor will not be automatically regenerated by the visual
     * editor.
     */
    public ExecutionMonitor() {
    }
    private DateFormat localDateFormat = null;

    /**
     * Builds main layout that represent as a split panel. Right side of it
     * contains monitor table, left side contains debugging view that shown
     * after request only.
     * 
 	 * @return mainLayout Panel with all components of Execution Monitor page.
 	 */
    private Panel buildMainLayout() {
        // common part: create layout
        mainLayout = new Panel("");
        hsplit = new HorizontalSplitPanel();
        mainLayout.setContent(hsplit);

        monitorTableLayout = new VerticalLayout();
        monitorTableLayout.setImmediate(true);
        monitorTableLayout.setMargin(true);
        monitorTableLayout.setSpacing(true);
        monitorTableLayout.setWidth("100%");
        monitorTableLayout.setHeight("100%");


        // top-level component properties

        setWidth("100%");
        setHeight("100%");

        //Layout for buttons Refresh and Clear Filters on the top.
        HorizontalLayout topLine = new HorizontalLayout();
        topLine.setSpacing(true);
        //topLine.setWidth(100, Unit.PERCENTAGE);
        //Refresh button. Refreshing the table
        Button refreshButton = new Button("Refresh", new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                refresh();
                //monitorTable.setVisibleColumns(visibleCols);
            }
        });
        topLine.addComponent(refreshButton);
        //topLine.setComponentAlignment(refreshButton, Alignment.MIDDLE_RIGHT);
        //Clear Filters button. Clearing filters on the table with executions.
        Button buttonDeleteFilters = new Button();
        buttonDeleteFilters.setCaption("Clear Filters");
        buttonDeleteFilters.setHeight("25px");
        buttonDeleteFilters.setWidth("110px");
        buttonDeleteFilters
                .addClickListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                monitorTable.resetFilters();
                monitorTable.setFilterFieldVisible("actions", false);
				monitorTable.setFilterFieldVisible("duration", false);
            }
        });
        topLine.addComponent(buttonDeleteFilters);
        //topLine.setComponentAlignment(buttonDeleteFilters, Alignment.MIDDLE_RIGHT);

//        Label topLineFiller = new Label();
//        topLine.addComponentAsFirst(topLineFiller);
//        topLine.setExpandRatio(topLineFiller, 1.0f);
        monitorTableLayout.addComponent(topLine);

        tableData = getTableData();

        //table with pipeline execution records
        monitorTable = new IntlibPagedTable();
        monitorTable.setSelectable(true);
        monitorTable.setContainerDataSource(tableData);
        monitorTable.setWidth("100%");
        monitorTable.setHeight("100%");
        monitorTable.setImmediate(true);
        monitorTable.setVisibleColumns(visibleCols); // Set visible columns
        monitorTable.setColumnHeaders(headers);
		monitorTable.setColumnWidth("obsolete", 60);
		monitorTable.setColumnWidth("status", 50);
		monitorTable.setColumnWidth("isDebugging", 50);
		monitorTable.setColumnWidth("duration", 60);
		monitorTable.setColumnWidth("actions", 200);

        //sorting by execution date
        Object property = "start";
        monitorTable.setSortContainerPropertyId(property);
        monitorTable.setSortAscending(false);
        monitorTable.sort();

        //Status column. Contains status icons.
        monitorTable.addGeneratedColumn("status", new CustomTable.ColumnGenerator() {
            @Override
            public Object generateCell(CustomTable source, Object itemId,
                    Object columnId) {
                PipelineExecutionStatus type = (PipelineExecutionStatus) source.getItem(itemId)
                        .getItemProperty(columnId).getValue();
                if(type != null) {
                ThemeResource img = IntlibHelper.getIconForExecutionStatus(type);
                Embedded emb = new Embedded(type.name(), img);
                emb.setDescription(type.name());
                return emb;
                } else {
                    return null;
                }
            }
        });

        //Debug column. Contains debug icons.
        monitorTable.addGeneratedColumn("isDebugging", new CustomTable.ColumnGenerator() {
            @Override
            public Object generateCell(CustomTable source, Object itemId,
                    Object columnId) {
                boolean inDebug = (boolean) source.getItem(itemId).getItemProperty(columnId).getValue();
                Embedded emb;
                if (inDebug) {
                    emb = new Embedded("True", new ThemeResource("icons/debug.png"));
                    emb.setDescription("TRUE");
                } else {
                    emb = new Embedded("False", new ThemeResource("icons/no_debug.png"));
                    emb.setDescription("FALSE");
                }
                return emb;
            }
        });
		
		monitorTable.addGeneratedColumn("duration", new CustomTable.ColumnGenerator() {

			@Override
			public Object generateCell(CustomTable source, Object itemId, Object columnId) {
				long duration = (long) source.getItem(itemId).getItemProperty(columnId).getValue();
				return IntlibHelper.formatDuration(duration);
			}
		});

        //Actions column. Contains actions buttons: Debug data, Show log, Cancel.
        monitorTable.addGeneratedColumn("actions",
                new GenerateActionColumnMonitor(this));

        monitorTableLayout.addComponent(monitorTable);
        monitorTableLayout.addComponent(monitorTable.createControls());
        monitorTable.setPageLength(20);
        monitorTable.setFilterDecorator(new filterDecorator());
        monitorTable.setFilterBarVisible(true);
        monitorTable.setFilterFieldVisible("actions", false);
		monitorTable.setFilterFieldVisible("duration", false);
        monitorTable.addItemClickListener(
                new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent event) {
                //debugging view open
                if (!monitorTable.isSelected(event.getItemId())) {
                    Long executionId = (long) event.getItem().getItemProperty("id").getValue();
                    showExecutionDetail(executionId);
                } else {
                }
            }
        });


        hsplit.setFirstComponent(monitorTableLayout);
        hsplit.setSecondComponent(null);
        hsplit.setSplitPosition(100, Unit.PERCENTAGE);
        hsplit.setLocked(true);

        monitorTable.refreshRowCache();
		
		App.getApp().getRefreshThread().setExecutionMonitor(this);

        return mainLayout;
    }

    /**
     * Calls for refresh {@link #monitorTable}.
     */
    public void refresh() {
        int page = monitorTable.getCurrentPage();
		Object selectedRow = monitorTable.getValue();
		IntlibLazyQueryContainer c = (IntlibLazyQueryContainer) monitorTable.getContainerDataSource().getContainer();
		c.refresh();
		monitorTable.setCurrentPage(page);
		monitorTable.setValue(selectedRow);
		//monitorTable.markAsDirty();
		//monitorTable.setVisibleColumns(visibleCols);
    }

    /**
     * Building layout with {@link DebuggingView}.
     * 
     * @return logLayout VerticalLayout contains {@link DebuggingView}
     */
    private VerticalLayout buildlogLayout() {

        logLayout = new VerticalLayout();
        logLayout.setImmediate(true);
        logLayout.setMargin(true);
        logLayout.setSpacing(true);
        logLayout.setWidth("100%");
        logLayout.setHeight("100%");

        PipelineExecution pipelineExec = App.getApp().getPipelines()
                .getExecution(exeId);
        DebuggingView debugView = new DebuggingView(pipelineExec, null,
                pipelineExec.isDebugging(), false);
//        debugView.addListener(new Listener() {
//            @Override
//            public void componentEvent(Event event) {
//                if (event.getComponent().getClass() == DebuggingView.class) {
//                    refreshData();
//                    monitorTable.setVisibleColumns(visibleCols);
//                }
//            }
//        });
        logLayout.addComponent(debugView);
        logLayout.setExpandRatio(debugView, 1.0f);

        //Layout for buttons  Close and  Export on the bottom
        HorizontalLayout buttonBar = new HorizontalLayout();
        buttonBar.setWidth("100%");

        Button buttonClose = new Button();
        buttonClose.setCaption("Close");
        buttonClose.setHeight("25px");
        buttonClose.setWidth("100px");
        buttonClose
                .addClickListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
				App.getApp().getRefreshThread().refreshExecution(null, null);

                hsplit.setSplitPosition(100, Unit.PERCENTAGE);
                hsplit.setLocked(true);
            }
        });
        buttonBar.addComponent(buttonClose);
        buttonBar.setComponentAlignment(buttonClose, Alignment.BOTTOM_LEFT);

        Button buttonExport = new Button();
        buttonExport.setCaption("Export");
        buttonExport.setHeight("25px");
        buttonExport.setWidth("100px");
        buttonExport
                .addClickListener(new com.vaadin.ui.Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
            }
        });
        buttonBar.addComponent(buttonExport);
        buttonBar.setComponentAlignment(buttonExport, Alignment.BOTTOM_RIGHT);

        logLayout.addComponent(buttonBar);
        logLayout.setExpandRatio(buttonBar, 0);
		
		if(pipelineExec.getStatus() == RUNNING || pipelineExec.getStatus() == SCHEDULED) {
			App.getApp().getRefreshThread().refreshExecution(pipelineExec, debugView);
		}
        return logLayout;

    }

    /**
     * Container with data for {@link #monitorTable}
     *
     * @param data. List of {@link PipelineExecution}
     * @return result. IndexedContainer with data for execution table
     */
    public static Container getTableData() {

        IntlibLazyQueryContainer result = new IntlibLazyQueryContainer<>(App.getApp().getLogs().getEntityManager(), PipelineExecution.class, 16, "id", true, true, true);
        result.getQueryView().getQueryDefinition().setDefaultSortState(
                new Object[]{"start"}, new boolean[]{true});
        result.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(2);
        for (String p : visibleCols) {
            // setting type of columns
            switch (p) {
                case "status":
                    result.addContainerProperty(p, PipelineExecutionStatus.class, null);
                    break;
                case "isDebugging":
                    result.addContainerProperty(p, Boolean.class, false);
                    break;
                case "start":
                    // Type used for date needs to be java.sql.Timestamp, because there
                    // seems to be a bug in com.vaadin.data.util.filter.Compare#compareValue
                    // that causes subclasses of java.util.Date to be uncomparable with
                    // its superclass java.util.Date.
                    // For details see github issue #135.
                    result.addContainerProperty(p, Timestamp.class, null);
                    break;
//                case "name":
//                    result.addContainerProperty("pipeline.name", String.class, "");
//                    break;
                default:
                    result.addContainerProperty(p, String.class, "");
                    break;
            }
        }

        result.addContainerProperty("id", Long.class, "");

//        for (PipelineExecution item : data) {
//
//            Object num = result.addItem();
//            if (item.getStart() == null) {
//                result.getContainerProperty(num, "date").setValue(null);
//            } else {
//
//                result.getContainerProperty(num, "date").setValue(
//                        item.getStart());
//            }
//            result.getContainerProperty(num, "id").setValue(item.getId());
//            result.getContainerProperty(num, "user").setValue(" ");
//            result.getContainerProperty(num, "name").setValue(
//                    item.getPipeline().getName());
//            result.getContainerProperty(num, "status").setValue(
//                    item.getExecutionStatus());
//            result.getContainerProperty(num, "debug").setValue(
//                    item.isDebugging());
//
//        }

        return result;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        buildMainLayout();
        setCompositionRoot(mainLayout);
		
		String strExecId = event.getParameters();
		if(strExecId == null || strExecId.isEmpty()) {
			return;
		}
		try {
			Long execId = Long.parseLong(strExecId);
			showExecutionDetail(execId);
		} catch(NumberFormatException e) {
			LOG.warn("Invalid parameter for execution monitor.", e);
		}
    }

    /**
     * Identify the button type was pushed (cancel, showlog, debug ) with the help
     * of {@link ActionButtonData}. Then produces a corresponding action. Opens
     * {@link DebuggingView}
     */
    @Override
    public void buttonClick(ClickEvent event) {
        Button senderButton = event.getButton();
        if (senderButton != null) {
            ActionButtonData senderData = (ActionButtonData) senderButton
                    .getData();
            String caption = senderData.action;
            Object itemId = senderData.data;

            Long execId = (Long) tableData.getContainerProperty(itemId, "id")
                    .getValue();
            switch (caption) {
                case "cancel":
                	PipelineExecution pipelineExec = 
                		App.getApp().getPipelines().getExecution(execId);
                	pipelineExec.stop();
                	App.getApp().getPipelines().save(pipelineExec);
                	senderButton.setVisible(false);
					refresh();
					Notification.show("Pipeline execution cancelled.", Notification.Type.HUMANIZED_MESSAGE);
                    break;
                case "showlog":
					showExecutionDetail(execId);
					break;
				case "debug":
					showExecutionDetail(execId);
					break;
            }

        }
    }

	@Override
	public boolean isModified() {
		//There are no editable fields.
		return false;
	}

	private void showExecutionDetail(Long executionId) {
		exeId = executionId;
		logLayout = buildlogLayout();
		hsplit.setSplitPosition(55, Unit.PERCENTAGE);
		hsplit.setSecondComponent(logLayout);
		hsplit.setLocked(false);
	}

    /**
     * Settings icons to the table filters "status" and "debug"
     *
     * @author Bogo
     *
     */
    class filterDecorator extends IntlibFilterDecorator {

        @Override
        public String getEnumFilterDisplayName(Object propertyId, Object value) {
            if (propertyId == "status") {
                return ((PipelineExecutionStatus) value).name();
            }
            return super.getEnumFilterDisplayName(propertyId, value);
        }

        @Override
        public Resource getEnumFilterIcon(Object propertyId, Object value) {
            if (propertyId == "status") {
                PipelineExecutionStatus type = (PipelineExecutionStatus) value;
                ThemeResource img = null;
                switch (type) {
                    case FINISHED_SUCCESS:
                        img = new ThemeResource("icons/ok.png");
                        break;
                    case FINISHED_WARNING:
                        img = new ThemeResource("icons/warning.png");
                        break;
                    case FAILED:
                        img = new ThemeResource("icons/error.png");
                        break;
                    case RUNNING:
                        img = new ThemeResource("icons/running.png");
                        break;
                    case SCHEDULED:
                        img = new ThemeResource("icons/scheduled.png");
                        break;
                    case CANCELLED:
                        img = new ThemeResource("icons/cancelled.png");
                        break;
                    default:
                        //no icon
                        break;
                }
                return img;
            }
            return super.getEnumFilterIcon(propertyId, value);
        }

        @Override
        public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
            if (propertyId.equals("isDebugging")) {
                if (value) {
                    return "Debug";
                } else {
                    return "Run";
                }
            }
            return super.getBooleanFilterDisplayName(propertyId, value);
        }

        @Override
        public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
            if (propertyId.equals("isDebugging")) {
                if (value) {
                    return new ThemeResource("icons/debug.png");
                } else {
                    return new ThemeResource("icons/no_debug.png");
                }
            }
            return super.getBooleanFilterIcon(propertyId, value);
        }
    };
}
