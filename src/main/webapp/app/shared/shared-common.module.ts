import { NgModule } from '@angular/core';

import { GooseGameSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [GooseGameSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [GooseGameSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class GooseGameSharedCommonModule {}
