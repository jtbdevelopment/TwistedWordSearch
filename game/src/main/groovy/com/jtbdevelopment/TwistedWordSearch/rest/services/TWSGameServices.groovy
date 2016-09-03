package com.jtbdevelopment.TwistedWordSearch.rest.services

import com.jtbdevelopment.TwistedWordSearch.rest.handlers.SubmitFindHandler
import com.jtbdevelopment.TwistedWordSearch.state.grid.GridCoordinate
import com.jtbdevelopment.games.rest.AbstractMultiPlayerGameServices
import groovy.transform.CompileStatic
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import javax.ws.rs.Consumes
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

/**
 * Date: 11/11/14
 * Time: 9:42 PM
 */
@Component
@CompileStatic
class TWSGameServices extends AbstractMultiPlayerGameServices<ObjectId> {
    @Autowired
    SubmitFindHandler submitFindHandler

    @PUT
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    Object findWord(final List<GridCoordinate> relativeCoordinates) {
        submitFindHandler.handleAction((ObjectId) playerID.get(), (ObjectId) gameID.get(), relativeCoordinates)
    }
}
