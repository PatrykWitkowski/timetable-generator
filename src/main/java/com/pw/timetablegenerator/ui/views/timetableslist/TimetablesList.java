package com.pw.timetablegenerator.ui.views.timetableslist;

import com.pw.timetablegenerator.backend.entity.Timetable;
import com.pw.timetablegenerator.backend.service.TimetableService;
import com.pw.timetablegenerator.backend.utils.security.SecurityUtils;
import com.pw.timetablegenerator.ui.MainLayout;
import com.pw.timetablegenerator.ui.common.AbstractEditorDialog;
import com.pw.timetablegenerator.ui.encoders.LocalDateToStringEncoder;
import com.pw.timetablegenerator.ui.encoders.LongToStringEncoder;
import com.pw.timetablegenerator.ui.encoders.TimetableTypeToStringEncoder;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.polymertemplate.EventHandler;
import com.vaadin.flow.component.polymertemplate.Id;
import com.vaadin.flow.component.polymertemplate.ModelItem;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.Encode;
import com.vaadin.flow.templatemodel.Exclude;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Displays the list of available orders, with a search filter as well as
 * buttons to add a new order or edit existing ones.
 *
 * Implemented using a simple template.
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Timetables")
@Tag("timetables-list")
@HtmlImport("frontend://src/views/timetableslist/timetables-list.html")
public class TimetablesList extends PolymerTemplate<TimetablesList.TimetablesModel> implements BeforeEnterObserver {

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(SecurityUtils.getCurrentUser() != null){
            updateList();
        }
    }

    public interface TimetablesModel extends TemplateModel {
        @Encode(value = LocalDateToStringEncoder.class, path = "generationDate")
        @Encode(value = LongToStringEncoder.class, path = "timetableId")
        @Encode(value = LongToStringEncoder.class, path = "semester")
        @Encode(value = LongToStringEncoder.class, path = "quality")
        @Encode(value = TimetableTypeToStringEncoder.class, path = "timetableType")
        @Exclude({"owner", "courses", "semesterStartDate", "semesterEndDate"})
        void setTimetables(List<Timetable> timetables);
    }

    @Id("search")
    private TextField search;
    @Id("newTimetable")
    private Button addTimetable;
    @Id("header")
    private H2 header;

    private TimetableEditorDialog timetableForm = new TimetableEditorDialog(
            this::saveUpdate, this::deleteUpdate);

    @Autowired
    private TimetableService timetableService;

    /**
     * Needs to be done here, because autowireds field are injected after constructor
     */
    @PostConstruct
    public void TimetablesList(){
        search.setPlaceholder("Search timetables");
        search.addValueChangeListener(e -> updateList());
        search.setValueChangeMode(ValueChangeMode.EAGER);

        addTimetable.addClickListener(e -> {
            final Timetable newTimetable = new Timetable(SecurityUtils.getCurrentUser().getUser());
            openForm(newTimetable, AbstractEditorDialog.Operation.ADD);
        });
    }

    public void saveUpdate(Timetable timetable,
                           AbstractEditorDialog.Operation operation){
        // generate new timetable for ADD, for EDIT nothing to do
    }

    public void deleteUpdate(Timetable timetable){
        //delete timetable
    }

    private void updateList() {
        final List<Timetable> timetables = timetableService.findTimetables(SecurityUtils.getCurrentUser().getUser(), search.getValue());
        if (search.isEmpty()) {
            header.setText("Orders");
            header.add(new Span(timetables.size() + " in total"));
        } else {
            header.setText("Search for “" + search.getValue() + "”");
            if (!timetables.isEmpty()) {
                header.add(new Span(timetables.size() + " results"));
            }
        }
        getModel().setTimetables(timetables);
    }

    @EventHandler
    private void edit(@ModelItem Timetable timetable) {
        final Timetable timetableToPreview = timetableService.findByTimetableId(timetable.getTimetableId());
        TimetablePreviewDialog previewDialog = new TimetablePreviewDialog(timetableToPreview);
        if (previewDialog.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(previewDialog));
        }
        previewDialog.open();
    }

    private void openForm(Timetable timetable,
                          AbstractEditorDialog.Operation operation) {
        // Add the form lazily as the UI is not yet initialized when
        // this view is constructed
        if (timetableForm.getElement().getParent() == null) {
            getUI().ifPresent(ui -> ui.add(timetableForm));
        }
        timetableForm.open(timetable, operation);
    }

}
