package com.rsetiapp.bhashini.RequestModel

data class TranslationRequest(
    val pipelineTasks: List<PipelineTask>,
    val inputData: InputData
)
