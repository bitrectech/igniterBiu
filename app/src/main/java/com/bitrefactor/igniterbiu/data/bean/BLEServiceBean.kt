package com.bitrefactor.igniterbiu.data.bean

import java.util.*
import kotlin.collections.ArrayList

data class BLEServiceBean(
    var uuid: UUID,
    var characteristicList:ArrayList<CharacteristicBean>
)