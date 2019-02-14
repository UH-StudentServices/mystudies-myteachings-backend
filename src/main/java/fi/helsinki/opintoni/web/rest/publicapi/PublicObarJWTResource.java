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

package fi.helsinki.opintoni.web.rest.publicapi;

import fi.helsinki.opintoni.dto.ObarJWTTokenDto;
import fi.helsinki.opintoni.integration.obar.ObarJWTService;
import fi.helsinki.opintoni.web.WebConstants;
import fi.helsinki.opintoni.web.rest.AbstractResource;
import fi.helsinki.opintoni.web.rest.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static fi.helsinki.opintoni.integration.obar.Constants.LANG_COOKIE_NAME;

@RestController
@RequestMapping(value = RestConstants.PUBLIC_API_V1, produces = WebConstants.APPLICATION_JSON_UTF8)
@ConditionalOnProperty(prefix = "obar", name = "baseUrl")
public class PublicObarJWTResource extends AbstractResource {

    private final ObarJWTService obarJWTService;

    @Autowired
    public PublicObarJWTResource(ObarJWTService obarJWTService) {
        this.obarJWTService = obarJWTService;
    }

    @GetMapping("/obar-jwt-token")
    public ResponseEntity<ObarJWTTokenDto> getObarJWTToken(@CookieValue(name = LANG_COOKIE_NAME, required = false) String obarLang) {
        return response(new ObarJWTTokenDto(obarJWTService.generateToken(null, obarLang)));
    }
}
