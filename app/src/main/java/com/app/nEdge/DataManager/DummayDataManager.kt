package com.app.nEdge.DataManager

import com.app.nEdge.models.MyDataClass
import com.app.nEdge.models.ResponseMain
import com.app.nEdge.models.ResponseModel
import retrofit2.Response

class DummayDataManager {

    companion object {
        fun getResponseDummyData(): Response<ResponseMain> {
            return Response.success(
                ResponseMain(
                    ResponseModel(
                        true,
                        "",
                        getDataList()
                    )
                )
            )
        }

        private fun getDataList(): MyDataClass {
            return MyDataClass(
                null
            )
        }


    }
}