import { useAtom, useAtomValue, useSetAtom } from "jotai";
import React, { useState } from "react";
import { getRoute } from "../../utils/map/geometryUtils";
import {
  advLevelAtom,
  showAdvChangeAtom,
  endAtom,
  endDateAtom,
  routeAtom,
  startAtom,
  startDateAtom,
  tripAtom,
  tripIdAtom,
  tripNameAtom,
  clearTripAtom,
} from "../../utils/atoms";
import Alert from "../auth/Alert";
import axios from "axios";
import { Button, OutlineButton } from "../general/Buttons";
import { useRouter } from "next/router";
import { tripToTripRequest } from "../../utils/trip";

const SaveModal = () => {
  const [show, setShow] = useAtom(showAdvChangeAtom);

  const [start, setStart] = useAtom(startAtom);
  const [end, setEnd] = useAtom(endAtom);
  const [startDate, setStartDate] = useAtom(startDateAtom);
  const [endDate, setEndDate] = useAtom(endDateAtom);
  const [activeLevel, setActiveLevel] = useAtom(advLevelAtom);
  const [route, setRoute] = useAtom(routeAtom);
  const [tripName, setTripName] = useAtom(tripNameAtom);
  const setTripId = useSetAtom(tripIdAtom);
  const trip = useAtomValue(tripAtom);
  const clearTrip = useSetAtom(clearTripAtom);

  const router = useRouter();
  const [alert, setAlert] = useState();

  const handleSubmit = (event) => {
    console.log("Handle submit");
    console.log(activeLevel);

    getRoute(start, end).then(
      (success) => {
        setRoute(success[0]);
        trip.route = success[0];


        axios.patch(`/api/trip/${trip.id}`, tripToTripRequest(trip)).then(
            (success) => {
              clearTrip();
              router.push("/trips/list/user");
            },
            (fail) => {
              setAlert("Trip failed to update");
            }
          );

        let drivingDays = Math.ceil(trip.route.duration / 86400.0);
        let requestedTime =
          new Date(endDate).getTime() - new Date(startDate).getTime();
        let requestedDays = requestedTime / (1000 * 3600 * 24) + 1;

        if (drivingDays > requestedDays) {
          setAlert("Trip not completable within time frame.");
        } else {
          axios.patch(`/api/trip/${trip.id}`, tripToTripRequest(trip)).then(
            (success) => {
              clearTrip();
              router.push("/trips/list/user");
            },
            (fail) => {
              setAlert("Trip failed to update");
            }
          );
        }
      },
      (error) => {
        setAlert("Cannot find a driving route between these locations.");
      }
    );
  };

  const handleRemove = (event) => {};

  return (
    <div
      className={`bg-gray-100 text-slate-800 text-sm p-4 w-1/2 h-fit ease-in-out duration-200 z-50 
            absolute top-24 left-1/2 -ml-[25%] rounded-lg border border-gray-200 shadow-xl ${
              show ? `translate-y-0` : `-translate-y-[30rem] bg-opacity-10`
            }`}
    >
      <form onSubmit={handleSubmit}>
        <h2 className="text-green-600 font-semibold text-xl">
          Adventure Level Change
        </h2>
        <div className="w-64 my-3">
          <div className="my-1 text-slate-900 font-semibold ml-0.5">
            You currently have stops that are above your adventure level. Would
            you like to keep or remove these stops?
          </div>
        </div>
        <div className="absolute bottom-4 right-4 gap-2 flex flex-row">
          <Button type="submit">Keep</Button>
          <Button onClick={handleRemove}>Remove</Button>
          <OutlineButton
            onClick={() => {
              setShow(false);
            }}
          >
            Cancel
          </OutlineButton>
        </div>
      </form>
    </div>
  );
};

export default SaveModal;
