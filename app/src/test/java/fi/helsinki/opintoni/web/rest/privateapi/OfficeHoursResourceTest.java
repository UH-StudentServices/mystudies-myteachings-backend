/*
 * This file is part of MystudiesMyteaching application.
 *
 * MystudiesMyteaching application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MystudiesMyteaching application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MystudiesMyteaching application.  If not, see <http://www.gnu.org/licenses/>.
 */

package fi.helsinki.opintoni.web.rest.privateapi;

import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.ADDITIONAL_INFO;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.ADDITIONAL_INFO_2;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.DEGREE_CODE_1;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.DEGREE_CODE_2;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.DEGREE_CODE_3;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.OFFICE_HOURS;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.OFFICE_HOURS_2;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.RECEPTION_LOCATION;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.RECEPTION_LOCATION_2;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.TEACHER_NAME;
import static fi.helsinki.opintoni.sampledata.OfficeHoursSampleData.TEACHER_NAME_2;
import static fi.helsinki.opintoni.security.SecurityRequestPostProcessors.securityContext;
import static fi.helsinki.opintoni.security.TestSecurityContext.teacherSecurityContext;
import static fi.helsinki.opintoni.web.WebTestUtils.toJsonBytes;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.collect.Lists;
import fi.helsinki.opintoni.SpringTest;
import fi.helsinki.opintoni.dto.DegreeProgrammeDto;
import fi.helsinki.opintoni.dto.OfficeHoursDto;
import fi.helsinki.opintoni.web.WebConstants;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;
import org.springframework.http.MediaType;

public class OfficeHoursResourceTest extends SpringTest {


    @Test
    public void thatOfficeHoursReturnCorrectResponse() throws Exception {
        mockMvc.perform(get("/api/private/v1/officehours")
            .with(securityContext(teacherSecurityContext()))
            .characterEncoding("UTF-8")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(WebConstants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$[0].description").value(OFFICE_HOURS))
            .andExpect(jsonPath("$[0].name").value(TEACHER_NAME))
            .andExpect(jsonPath("$[0].additionalInfo").value(ADDITIONAL_INFO))
            .andExpect(jsonPath("$[0].receptionLocation").value(RECEPTION_LOCATION));
    }

    @Test
    public void thatOfficeHoursAreUpdated() throws Exception {
        DegreeProgrammeDto programme1 = new DegreeProgrammeDto();
        DegreeProgrammeDto programme2 = new DegreeProgrammeDto();
        programme1.code = DEGREE_CODE_1;
        programme2.code = DEGREE_CODE_2;
        OfficeHoursDto officeHoursDto = new OfficeHoursDto(TEACHER_NAME, OFFICE_HOURS,
            ADDITIONAL_INFO_2, RECEPTION_LOCATION_2, Lists.newArrayList(programme1, programme2));

        List<OfficeHoursDto> request = Arrays.asList(officeHoursDto);

        mockMvc.perform(post("/api/private/v1/officehours")
            .with(securityContext(teacherSecurityContext()))
            .characterEncoding("UTF-8")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJsonBytes(request))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(WebConstants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$[0].description").value(OFFICE_HOURS))
            .andExpect(jsonPath("$[0]name").value(TEACHER_NAME))
            .andExpect(jsonPath("$[0]additionalInfo").value(ADDITIONAL_INFO_2))
            .andExpect(jsonPath("$[0]receptionLocation").value(RECEPTION_LOCATION_2))
            .andExpect(jsonPath("$[0]degreeProgrammes").isArray())
            .andExpect(jsonPath("$[0]degreeProgrammes", hasSize(2)))
            .andExpect(jsonPath("$[0]degreeProgrammes[0].code").value(DEGREE_CODE_1))
            .andExpect(jsonPath("$[0]degreeProgrammes[1].code").value(DEGREE_CODE_2));
    }

    @Test
    public void thatOfficeHoursAreDeleted() throws Exception {
        mockMvc.perform(delete("/api/private/v1/officehours")
            .with(securityContext(teacherSecurityContext()))
            .characterEncoding("UTF-8")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andExpect(content().string(""));
    }

    @Test
    public void thatMultipleOfficeHoursCanBeAdded() throws Exception {
        OfficeHoursDto officeHoursDto = new OfficeHoursDto(
            TEACHER_NAME,
            OFFICE_HOURS,
            null,
            null,
            createProgrammeDtoList(DEGREE_CODE_1, DEGREE_CODE_2)
        );

        OfficeHoursDto officeHoursDto2 = new OfficeHoursDto(
            TEACHER_NAME_2,
            OFFICE_HOURS_2,
            null,
            null,
            createProgrammeDtoList(DEGREE_CODE_3)
        );

        List<OfficeHoursDto> request = Arrays.asList(officeHoursDto, officeHoursDto2);

        mockMvc.perform(post("/api/private/v1/officehours")
            .with(securityContext(teacherSecurityContext()))
            .characterEncoding("UTF-8")
            .contentType(MediaType.APPLICATION_JSON)
            .content(toJsonBytes(request))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(WebConstants.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$[0].description").value(OFFICE_HOURS))
            .andExpect(jsonPath("$[0]name").value(TEACHER_NAME))
            .andExpect(jsonPath("$[0]degreeProgrammes").isArray())
            .andExpect(jsonPath("$[0]degreeProgrammes", hasSize(2)))
            .andExpect(jsonPath("$[0]degreeProgrammes[0].code").value(DEGREE_CODE_1))
            .andExpect(jsonPath("$[0]degreeProgrammes[1].code").value(DEGREE_CODE_2))
            .andExpect(jsonPath("$[1].description").value(OFFICE_HOURS_2))
            .andExpect(jsonPath("$[1]name").value(TEACHER_NAME_2))
            .andExpect(jsonPath("$[1]degreeProgrammes").isArray())
            .andExpect(jsonPath("$[1]degreeProgrammes", hasSize(1)))
            .andExpect(jsonPath("$[1]degreeProgrammes[0].code").value(DEGREE_CODE_3));
    }

    private List<DegreeProgrammeDto> createProgrammeDtoList(String... codes) {
        return Arrays.stream(codes).map(code -> {
            DegreeProgrammeDto dto = new DegreeProgrammeDto();
            dto.code = code;
            return dto;
        }).collect(Collectors.toList());
    }
}
