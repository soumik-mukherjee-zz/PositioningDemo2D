SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
﻿CREATE TABLE "public"."VehicleLocations" (gid serial,"vehicleid" integer not null, "updatedon" timestamp);
ALTER TABLE "public"."VehicleLocations" ADD PRIMARY KEY (gid);
ALTER TABLE public."VehicleLocations" ADD CONSTRAINT "VehicleLocation_bizkey" UNIQUE (vehicleid, updatedon);
ALTER TABLE public."VehicleLocations"
  ADD CONSTRAINT "Vehicle_Location_fkey" FOREIGN KEY (vehicleid) REFERENCES public."Vehicles" (gid)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
SELECT AddGeometryColumn('public','VehicleLocations','location','4326','POINT',2);
COMMIT;
