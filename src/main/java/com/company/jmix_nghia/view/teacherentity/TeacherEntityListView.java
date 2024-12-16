package com.company.jmix_nghia.view.teacherentity;

import com.company.jmix_nghia.entity.TeacherEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstanceLoader;
import io.jmix.flowui.view.*;

@Route(value = "teacherEntities", layout = MainView.class)
@ViewController(id = "TeacherEntity.list")
@ViewDescriptor(path = "teacher-entity-list-view.xml")
@LookupComponent("teacherEntitiesDataGrid")
@DialogMode(width = "64em")
public class TeacherEntityListView extends StandardListView<TeacherEntity> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<TeacherEntity> teacherEntitiesDc;

    @ViewComponent
    private InstanceContainer<TeacherEntity> teacherEntityDc;

    @ViewComponent
    private InstanceLoader<TeacherEntity> teacherEntityDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("teacherEntitiesDataGrid.create")
    public void onTeacherEntitiesDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        TeacherEntity entity = dataContext.create(TeacherEntity.class);
        teacherEntityDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("teacherEntitiesDataGrid.edit")
    public void onTeacherEntitiesDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        TeacherEntity item = teacherEntityDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        teacherEntitiesDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        teacherEntityDl.load();
        updateControls(false);
    }

    @Subscribe(id = "teacherEntitiesDc", target = Target.DATA_CONTAINER)
    public void onTeacherEntitiesDcItemChange(final InstanceContainer.ItemChangeEvent<TeacherEntity> event) {
        TeacherEntity entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            teacherEntityDl.setEntityId(entity.getId());
            teacherEntityDl.load();
        } else {
            teacherEntityDl.setEntityId(null);
            teacherEntityDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(TeacherEntity entity) {
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