package enterprise.dto;

import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class EmployeeDTOValidator implements Validator
{

    @Autowired
    private javax.validation.Validator validator;

    @Override
    public boolean supports(Class<?> clazz)
    {
        return EmployeeDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors)
    {
        Set<ConstraintViolation<Object>> validates = validator.validate(target);

        for (ConstraintViolation<Object> constraintViolation : validates)
        {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.rejectValue(propertyPath, "", message);
        }

        EmployeeDTO dto = (EmployeeDTO) target;
        if (dto.getBornDate() != null && dto.getEmploymentDate() != null &&
                (dto.getBornDate().isAfter(dto.getEmploymentDate()) || dto.getBornDate().isEqual(dto.getEmploymentDate())))
        {
            errors.rejectValue("bornDate", "value.bornDate2employmentDate");
        }

    }

}
