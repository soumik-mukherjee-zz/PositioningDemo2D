SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
﻿CREATE TABLE "public"."Proximity" (gid serial,xfrid integer not null, eventon timestamp not null);
ALTER TABLE "public"."Proximity" ADD PRIMARY KEY (gid);
ALTER TABLE public."Proximity"
  ADD CONSTRAINT "Proximity_bizkey" UNIQUE (xfrid, eventon);
COMMIT;
