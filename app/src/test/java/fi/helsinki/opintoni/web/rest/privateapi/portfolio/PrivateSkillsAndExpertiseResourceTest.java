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

package fi.helsinki.opintoni.web.rest.privateapi.portfolio;

import fi.helsinki.opintoni.SpringTest;
import fi.helsinki.opintoni.dto.portfolio.SkillsAndExpertiseDto;
import fi.helsinki.opintoni.repository.portfolio.SkillsAndExpertiseRepository;
import fi.helsinki.opintoni.web.WebTestUtils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static fi.helsinki.opintoni.security.SecurityRequestPostProcessors.securityContext;
import static fi.helsinki.opintoni.security.TestSecurityContext.studentSecurityContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PrivateSkillsAndExpertiseResourceTest extends SpringTest {

    @Autowired
    private SkillsAndExpertiseRepository skillsAndExpertiseRepository;

    @Test
    public void thatSkillsAndExpertiseIsUpdated() throws Exception {
        final String newSkillsAndExpertise = "New skills and expertise content";
        SkillsAndExpertiseDto request = new SkillsAndExpertiseDto();
        request.skillsAndExpertise = newSkillsAndExpertise;

        mockMvc.perform(post("/api/private/v1/portfolio/2/skillsandexpertise")
            .with(securityContext(studentSecurityContext()))
            .contentType(MediaType.APPLICATION_JSON)
            .content(WebTestUtils.toJsonBytes(request)))
            .andExpect(status().isOk());

        assertThat(skillsAndExpertiseRepository.findByPortfolioId(2L).get().skillsAndExpertise).isEqualTo(newSkillsAndExpertise);
    }
}
