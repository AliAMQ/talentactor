import { Injectable } from '@angular/core';
import * as AWS from 'aws-sdk/global';
import * as S3 from 'aws-sdk/clients/s3';
import { v4 as uuid } from 'uuid';

@Injectable({
    providedIn: 'root'
})
export class FileManagementService {
    cloudAddress = 'https://s3.amazonaws.com/';
    bucketName = 'alirezaamqgenerals3uploadbucketbetaapp';
    folder = 'talentactor/';

    constructor() {}

    getCloud(): string {
        return this.cloudAddress + this.bucketName + '/' + this.folder;
    }

    private getS3Bucket(): any {
        const bucket = new S3({
            accessKeyId: '',
            secretAccessKey: '',
            region: 'us-east-1'
        });

        return bucket;
    }

    uploadfile(file): string {
        const uid = uuid();
        const params = {
            Bucket: this.bucketName,
            Key: this.folder + uid + file.name,
            Body: file,
            ACL: 'public-read',
            ContentType: 'image'
        };

        this.getS3Bucket().upload(params, function(err, data) {
            if (err) {
                console.log('There was an error uploading your file: ', err);
                return false;
            }

            console.log('Successfully uploaded file.', data);
            return true;
        });
        return uid + file.name;
    }

    deleteFile(filename) {
        const params = {
            Bucket: this.bucketName,
            Key: this.folder + filename
        };
        // this.getS3Bucket()
        this.getS3Bucket().deleteObject(params, function(err, data) {
            if (err) {
                console.log('There was an error deleting your file: ', err.message);
                return;
            }
            console.log('Successfully deleted file.');
        });
    }
}
