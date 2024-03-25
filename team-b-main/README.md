# team-b

## Lab 7 to-do

### Implement opt. 2 features 5 + 8

5: search for a stop by stopID and all routeIDs contained in that stop
 - [X] change order of file imports so stops, routes, stopTimes
 - [ ] disable option: user can't search for routeIDs if ....
 - [X] create List in all stop instances
 - [X] add route refs to designated stop during import


8: search for a stop by stopID and display the tripIDs closest to the current times
 - [X] add time variable to stopTimes (EB)
 - [X] use stopTripMap to get all tripIDs for a stop (EB)
 - [ ] get all times -> search by stopID in stopTimesMap (EB)
 - [ ] compare trips times to time stamp and find next trip to display (EB)


### Lab Checklist (what needs to be turned in/done)
 - [X] dev branch created
 - [ ] use merge request to check work into main branch when done
 - [ ] turn in a PDF to canvas indicating which features where implemented
 - [ ] turn in PDF to canvas w/ meeting notes
 - [ ] add comments to all code w/ @author tag to every method
 - [ ] throw exception + validate if GPS values are out of range
 - [ ] Search by trip w/ no data throws unhandled exception
 - [ ] if a trip doesn't have route ID -> don't import file
 - [ ] Debugging console output should be removed
 - [ ] Routes missing routeID -> don't import file
 - [ ] Negative sequence values in stop_times shouldn't be allowed to load
 - [ ] Entering a trip with no data throws an exception