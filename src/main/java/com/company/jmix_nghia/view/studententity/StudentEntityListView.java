package com.company.jmix_nghia.view.studententity;

import com.company.jmix_nghia.entity.StudentEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.*;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;

@Route(value = "studentEntities", layout = MainView.class)
@ViewController(id = "StudentEntity.list")
@ViewDescriptor(path = "student-entity-list-view.xml")
@LookupComponent("studentEntitiesDataGrid")
@DialogMode(width = "64em")
public class StudentEntityListView extends StandardListView<StudentEntity> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<StudentEntity> studentEntitiesDc;

    @ViewComponent
    private InstanceContainer<StudentEntity> studentEntityDc;

    @ViewComponent
    private InstanceLoader<StudentEntity> studentEntityDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private DataGrid<StudentEntity> studentEntitiesDataGrid;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe(id = "detailButton", subject = "clickListener")
    public void onDetailButtonClick(final ClickEvent<JmixButton> event) {
        StudentEntity student = studentEntitiesDataGrid.getSingleSelectedItem();
        // Xây dựng dialog và mở view
        dialogWindows.detail(this, StudentEntity.class)
                .withViewClass(StudentEntityDetailView.class)
                .editEntity(student)
                .build().open();
    }

    @Subscribe("studentEntitiesDataGrid.create")
    public void onStudentEntitiesDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        StudentEntity entity = dataContext.create(StudentEntity.class);
        studentEntityDc.setItem(entity);
        updateControls(true);
    }

//    @Subscribe("studentEntitiesDataGrid.edit")
//    public void onStudentEntitiesDataGridEdit(final ActionPerformedEvent event) {
//        updateControls(true);
//    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        StudentEntity item = studentEntityDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        studentEntitiesDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        studentEntityDl.load();
        updateControls(false);
    }

    @Subscribe(id = "studentEntitiesDc", target = Target.DATA_CONTAINER)
    public void onStudentEntitiesDcItemChange(final InstanceContainer.ItemChangeEvent<StudentEntity> event) {
        StudentEntity entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            studentEntityDl.setEntityId(entity.getId());
            studentEntityDl.load();
        } else {
            studentEntityDl.setEntityId(null);
            studentEntityDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(StudentEntity entity) {
        ViewValidation viewValidation = getViewValidation();
        ValidationErrors validationErrors = viewValidation.validateUiComponents(form);
        if (!validationErrors.isEmpty()) {
            return validationErrors;
        }
        validationErrors.addAll(viewValidation.validateBeanGroup(UiCrossFieldChecks.class, entity));
        return validationErrors;
    }

    private void updateControls(boolean editing) {
        UiComponentUtils.getComponents(form).forEach(component -> {
            if (component instanceof HasValueAndElement<?, ?> field) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }

}