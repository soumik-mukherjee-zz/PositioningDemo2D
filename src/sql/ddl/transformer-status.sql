SET CLIENT_ENCODING TO UTF8;
SET STANDARD_CONFORMING_STRINGS TO ON;
BEGIN;
CREATE TABLE "public"."TransformerStatus" (gid serial, xfrid integer not null, status char(3), data varchar(4000));
ALTER TABLE "public"."TransformerStatus" ADD PRIMARY KEY (gid);
ALTER TABLE public."TransformerStatus"
  ADD CONSTRAINT "TransformerStatus_bizkey" UNIQUE (xfrid);
COMMIT;
