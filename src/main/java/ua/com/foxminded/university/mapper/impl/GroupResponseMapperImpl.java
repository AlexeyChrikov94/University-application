package ua.com.foxminded.university.mapper.impl;

import org.springframework.stereotype.Component;
import ua.com.foxminded.university.dto.GroupResponse;
import ua.com.foxminded.university.entity.Group;
import ua.com.foxminded.university.mapper.interfaces.DepartmentResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.FormOfEducationResponseMapper;
import ua.com.foxminded.university.mapper.interfaces.GroupResponseMapper;

@Component
public class GroupResponseMapperImpl implements GroupResponseMapper {

    private final DepartmentResponseMapper departmentResponseMapper;
    private final FormOfEducationResponseMapper formOfEducationResponseMapper;

    public GroupResponseMapperImpl(DepartmentResponseMapper departmentResponseMapper,
                                   FormOfEducationResponseMapper formOfEducationResponseMapper) {
        this.departmentResponseMapper = departmentResponseMapper;
        this.formOfEducationResponseMapper = formOfEducationResponseMapper;
    }

    @Override
    public GroupResponse mapEntityToDto(Group entity) {
        GroupResponse groupResponse = new GroupResponse();
        if (entity == null) {
            return null;
        } else if (entity.getId() == 0L){
            groupResponse.setId(0L);
            groupResponse.setName("");
            groupResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));
            groupResponse.setFormOfEducationResponse(formOfEducationResponseMapper.mapEntityToDto(entity.getFormOfEducation()));
        } else {
            groupResponse.setId(entity.getId());
            groupResponse.setName(entity.getName());
            groupResponse.setDepartmentResponse(departmentResponseMapper.mapEntityToDto(entity.getDepartment()));
            groupResponse.setFormOfEducationResponse(formOfEducationResponseMapper.mapEntityToDto(entity.getFormOfEducation()));
        }

        return groupResponse;
    }

}
