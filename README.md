# oci-java-sdk-copyobject-test

## prerequisite

You need to have oci config file at `~/.oci/config`, this test will use `DEFAULT` profile.

You need to have a bucket named `test-bucket` and object named `test-source`, this test will copy the object to the destination named `test-destination`.

## Run

`mvnw java:exec`

## Result

You will see the request body contains "destinationObjectMetadata" attribute with null as below, although the attribute is NOT set in the source code explicitly. 

```
{"sourceObjectName":"test-source","destinationRegion":"xx-xxxxx-x","destinationNamespace":"xxxxxxxxxxxx","destinationBucket":"test-bucket","destinationObjectName":"test-destination","destinationObjectMetadata":null}
```
