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

package fi.helsinki.opintoni.task;

import fi.helsinki.opintoni.SpringTest;
import fi.helsinki.opintoni.integration.coursepage.CoursePageClient;
import fi.helsinki.opintoni.integration.coursepage.CoursePageCourseImplementation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

import static fi.helsinki.opintoni.web.TestConstants.TEACHER_COURSE_REALISATION_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class CourseImplementationCacheBusterTest extends SpringTest {
    @Autowired
    private CoursePageClient coursePageRestClient;

    @Autowired
    private CourseImplementationCacheBuster courseImplementationCacheBuster;

    @Test
    public void thatCourseImplementationCacheIsBusted() {
        defaultTeacherRequestChain().defaultCourseImplementation();
        expectCourseImplementationChangesRequest();
        defaultTeacherRequestChain().defaultCourseImplementation();

        CoursePageCourseImplementation implementations = coursePageRestClient.getCoursePage(TEACHER_COURSE_REALISATION_ID);

        courseImplementationCacheBuster.checkForUpdatedCourseImplementations(Instant.now().getEpochSecond());

        CoursePageCourseImplementation implementationsAfterCacheBust =
            coursePageRestClient.getCoursePage(TEACHER_COURSE_REALISATION_ID);

        assertThat(implementations).isNotSameAs(implementationsAfterCacheBust);
    }
}
