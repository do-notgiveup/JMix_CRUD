package com.company.jmix_nghia.view.schoolentity;

import com.company.jmix_nghia.entity.SchoolEntity;
import com.company.jmix_nghia.view.main.MainView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.core.validation.group.UiCrossFieldChecks;
import io.jmix.flowui.component.UiComponentUtils;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.component.validation.ValidationErrors;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.*;
import io.jmix.flowui.view.*;

import java.util.HashMap;
import java.util.Map;

@Route(value = "schoolEntities", layout = MainView.class)
@ViewController(id = "SchoolEntity.list")
@ViewDescriptor(path = "school-entity-list-view.xml")
@LookupComponent("schoolEntitiesDataGrid")
@DialogMode(width = "64em")
public class SchoolEntityListView extends StandardListView<SchoolEntity> {

    @ViewComponent
    private DataContext dataContext;

    @ViewComponent
    private CollectionContainer<SchoolEntity> schoolEntitiesDc;

    @ViewComponent
    private InstanceContainer<SchoolEntity> schoolEntityDc;

    @ViewComponent
    private InstanceLoader<SchoolEntity> schoolEntityDl;

    @ViewComponent
    private VerticalLayout listLayout;

    @ViewComponent
    private FormLayout form;

    @ViewComponent
    private HorizontalLayout detailActions;
    @ViewComponent
    private TypedTextField<String> nameField;
    @ViewComponent
    private TypedTextField<String> schoolCodeField;
    @ViewComponent
    private CollectionLoader<SchoolEntity> schoolEntitiesDl;

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        updateControls(false);
    }

    @Subscribe("schoolEntitiesDataGrid.create")
    public void onSchoolEntitiesDataGridCreate(final ActionPerformedEvent event) {
        dataContext.clear();
        SchoolEntity entity = dataContext.create(SchoolEntity.class);
        schoolEntityDc.setItem(entity);
        updateControls(true);
    }

    @Subscribe("schoolEntitiesDataGrid.edit")
    public void onSchoolEntitiesDataGridEdit(final ActionPerformedEvent event) {
        updateControls(true);
    }

    @Subscribe("schoolEntitiesDataGrid.refreshAction")
    public void onSchoolEntitiesDataGriRefresh(final ActionPerformedEvent event) {
//        updateControls(true);
        String query = "select e from SchoolEntity e where 1 = 1";
        Map<String, Object> params = new HashMap<>();

        if (nameField != null && !nameField.getValue().isEmpty()) {
            query += " and e.name like concat('%', :nameField, '%')";
            params.put("nameField", nameField.getValue());
        }

        if (schoolCodeField != null && !schoolCodeField.getValue().isEmpty()) {
            query += " and e.schoolCode like concat('%', :schoolCodeField, '%')";
            params.put("schoolCodeField", schoolCodeField.getValue());
        }

        // Sắp xếp kết quả theo `id`
        query += " order by e.id desc";

        // Đặt câu truy vấn và các tham số cho `DataLoader`
        schoolEntitiesDl.setQuery(query);
        schoolEntitiesDl.setParameters(params);
        schoolEntitiesDl.setFirstResult(0);
        schoolEntitiesDl.load();
    }

    @Subscribe("saveButton")
    public void onSaveButtonClick(final ClickEvent<JmixButton> event) {
        SchoolEntity item = schoolEntityDc.getItem();
        ValidationErrors validationErrors = validateView(item);
        if (!validationErrors.isEmpty()) {
            ViewValidation viewValidation = getViewValidation();
            viewValidation.showValidationErrors(validationErrors);
            viewValidation.focusProblemComponent(validationErrors);
            return;
        }
        dataContext.save();
        schoolEntitiesDc.replaceItem(item);
        updateControls(false);
    }

    @Subscribe("cancelButton")
    public void onCancelButtonClick(final ClickEvent<JmixButton> event) {
        dataContext.clear();
        schoolEntityDl.load();
        updateControls(false);
    }

    @Subscribe(id = "schoolEntitiesDc", target = Target.DATA_CONTAINER)
    public void onSchoolEntitiesDcItemChange(final InstanceContainer.ItemChangeEvent<SchoolEntity> event) {
        SchoolEntity entity = event.getItem();
        dataContext.clear();
        if (entity != null) {
            schoolEntityDl.setEntityId(entity.getId());
            schoolEntityDl.load();
        } else {
            schoolEntityDl.setEntityId(null);
            schoolEntityDc.setItem(null);
        }
        updateControls(false);
    }

    protected ValidationErrors validateView(SchoolEntity entity) {
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
                    && !(component.getId().get().equals("nameField") || component.getId().get().equals("schoolCodeField"))
            ){
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