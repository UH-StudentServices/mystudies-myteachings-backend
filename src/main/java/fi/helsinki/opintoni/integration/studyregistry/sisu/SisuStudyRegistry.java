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

package fi.helsinki.opintoni.integration.studyregistry.sisu;

import fi.helsinki.opintoni.integration.studyregistry.Enrollment;
import fi.helsinki.opintoni.integration.studyregistry.Event;
import fi.helsinki.opintoni.integration.studyregistry.Person;
import fi.helsinki.opintoni.integration.studyregistry.StudyAttainment;
import fi.helsinki.opintoni.integration.studyregistry.StudyRegistry;
import fi.helsinki.opintoni.integration.studyregistry.StudyRight;
import fi.helsinki.opintoni.integration.studyregistry.Teacher;
import fi.helsinki.opintoni.integration.studyregistry.TeacherCourse;
import fi.helsinki.opintoni.integration.studyregistry.sisu.model.PrivatePersonRequest;
import fi.helsinki.opintoni.integration.studyregistry.sisu.model.StudyAttainmentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("sisuStudyRegistry")
public class SisuStudyRegistry implements StudyRegistry {

    private final SisuClient sisuClient;
    private final SisuStudyRegistryConverter sisuStudyRegistryConverter;

    @Autowired
    public SisuStudyRegistry(SisuClient sisuClient, SisuStudyRegistryConverter sisuStudyRegistryConverter) {
        this.sisuClient = sisuClient;
        this.sisuStudyRegistryConverter = sisuStudyRegistryConverter;
    }

    @Override
    public List<Enrollment> getEnrollments(String studentNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> getStudentEvents(String studentNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Event> getTeacherEvents(String teacherNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StudyAttainment> getStudyAttainments(String personId) {
        StudyAttainmentRequest studyAttainmentRequest = sisuClient.getStudyAttainments(personId);
        return studyAttainmentRequest.attainments.stream()
            .map(sisuStudyRegistryConverter::sisuAttainmentToStudyAttainment)
            .collect(Collectors.toList());
    }

    @Override
    public List<StudyAttainment> getStudyAttainments(String personId, String studentNumber) {
        return getStudyAttainments(personId);
    }

    @Override
    public List<TeacherCourse> getTeacherCourses(String teacherNumber, LocalDate since) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<StudyRight> getStudentStudyRights(String studentNumber) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Teacher> getCourseRealisationTeachers(String realisationId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Person getPerson(String personId) {
        PrivatePersonRequest privatePersonRequest = sisuClient.getPrivatePerson(personId);
        return sisuStudyRegistryConverter.sisuPrivatePersonToPerson(privatePersonRequest);
    }
}
