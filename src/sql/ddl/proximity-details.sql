SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
﻿CREATE TABLE "public"."ProximityDetails" (gid serial,proximityid integer not null, vehicleid integer not null);
ALTER TABLE "public"."ProximityDetails" ADD PRIMARY KEY (gid);
ALTER TABLE public."ProximityDetails"
  ADD CONSTRAINT "ProximityDetails_bizkey" UNIQUE (proximityid, vehicleid);
ALTER TABLE public."ProximityDetails"
  ADD CONSTRAINT "Proximity_Details_fkey" FOREIGN KEY (proximityid) REFERENCES public."Proximity" (gid)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
COMMIT;
