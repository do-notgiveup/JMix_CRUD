package com.company.jmix_nghia.view.classentity;

import com.company.jmix_nghia.entity.ClassEntity;
import com.company.jmix_nghia.entity.SchoolEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.EntityStates;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.action.list.CreateAction;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.*;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@Route(value = "classEntities", layout = MainView.class)
@ViewController(id = "ClassEntity.list")
@ViewDescriptor(path = "class-entity-list-view.xml")
@LookupComponent("classEntitiesDataGrid")
@DialogMode(width = "64em")
public class ClassEntityListView extends StandardListView<ClassEntity> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<ClassEntity> classEntitiesDc;

    @ViewComponent
    private InstanceContainer<ClassEntity> classEntityDc;

    @ViewComponent
    private InstanceLoader<ClassEntity> classEntityDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;

    @ViewComponent
    private TypedTextField<Object> nameField;

    @ViewComponent
    private TypedTextField<String> classCodeField;

    @ViewComponent
    private CollectionLoader<ClassEntity> classEntitiesDl;
    @ViewComponent
    private JmixButton searchButton;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {

        updateControls(false);
//        classCodeField.setReadOnly(false);
//        nameField.setReadOnly(false);
//        searchButton.setEnabled(true);
    }

    @Subscribe("classEntitiesDataGrid.create")
    public void onClassEntitiesDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        ClassEntity entity = dataContext.create(ClassEntity.class);
        classEntityDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("classEntitiesDataGrid.edit")
    public void onClassEntitiesDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("classEntitiesDataGrid.refreshAction")
    public void onClassEntitiesDataGriRefresh(final ActionPerformedEvent event) {
//        updateControls(true);
        String query = "select e from ClassEntity e where 1 = 1";
        Map<String, Object> params = new HashMap<>();

        if (nameField != null && !nameField.getValue().isEmpty()) {
            query += " and (e.name like concat('%', :nameField, '%')";
            params.put("nameField", nameField.getValue());
        }

        if (classCodeField != null && !classCodeField.getValue().isEmpty()) {
            query += " and (e.classCode like concat('%', :classCodeField, '%')";
            params.put("classCodeField", classCodeField.getValue());
        }

        // Sắp xếp kết quả theo `id`
        query += " order by e.id desc";

        // Đặt câu truy vấn và các tham số cho `DataLoader`
        classEntitiesDl.setQuery(query);
        classEntitiesDl.setParameters(params);
        classEntitiesDl.setFirstResult(0);
        classEntitiesDl.load();
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        ClassEntity item = classEntityDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        classEntitiesDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        classEntityDl.load();
        updateControls(false);
    }

    @Subscribe(id = "classEntitiesDc", target = Target.DATA_CONTAINER)
    public void onClassEntitiesDcItemChange(final InstanceContainer.ItemChangeEvent<ClassEntity> event) {
        ClassEntity entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            classEntityDl.setEntityId(entity.getId());
            classEntityDl.load();
        } else {
            classEntityDl.setEntityId(null);
            classEntityDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(ClassEntity entity) {
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
            if ((component instanceof HasValueAndElement<?, ?> field)
                    && !(component.getId().get().equals("nameField") || component.getId().get().equals("classCodeField"))
            ) {
                field.setReadOnly(!editing);
            }
        });

        detailActions.setVisible(editing);
        listLayout.setEnabled(!editing);
    }

    private ViewValidation getViewValidation() {
        return getApplicationContext().getBean(ViewValidation.class);
    }

//    @Subscribe(id = "searchButton", subject = "clickListener")
//    public void onSearchButtonClick(final ClickEvent<JmixButton> event) {
//        updateControls(true);
//        String query = "select e from ClassEntity e where 1 = 1";
//        Map<String, Object> params = new HashMap<>();
//
//        if (nameField != null && !nameField.getValue().isEmpty()) {
//            query += " and (e.name = :nameField)";
//            params.put("nameField", nameField.getValue());
//        }
//
//        if (classCodeField != null && !classCodeField.getValue().isEmpty()) {
//            query += " and (e.classCode = :classCodeField)";
//            params.put("classCodeField", classCodeField.getValue());
//        }
//
//        // Sắp xếp kết quả theo `id`
//        query += " order by e.id desc";
//
//        // Đặt câu truy vấn và các tham số cho `DataLoader`
//        classEntitiesDl.setQuery(query);
//        classEntitiesDl.setParameters(params);
//        classEntitiesDl.setFirstResult(0);
//        classEntitiesDl.load();
//    }

}