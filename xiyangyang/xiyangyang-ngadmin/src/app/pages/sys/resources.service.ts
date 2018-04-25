import { Injectable } from '@angular/core';
import {Observable} from '../../../../node_modules/rxjs';
import {Resource} from './resource.model';

@Injectable()
export class ResourcesService {

  dbData = [];
  constructor() {
    for (let i = 0; i <= 200; i++) {
      const resource = new Resource();
      resource.id = '' + (1000 + i);
      resource.name = 'Resource' + i;
      resource.code = 'code' + i ;
      resource.description = 'description' + i;
      resource.type = 'Image';
      this.dbData.push(resource);
    }
  }

  getData(pageNumber: number, pageSize: number) {
    const total = 200;
    const data = [];

    const endIndex = pageSize * pageNumber;
    const startIndex = pageSize * (pageNumber - 1) + 1;

    for (let i = startIndex; i <= endIndex; i++) {
      const resource = this.dbData[i];
      data.push(resource);
    }
    return Observable.of({
      rows: data,
      total: total,
      pageNumber: pageNumber,
      pageSize: pageSize,
    });

  }

}
