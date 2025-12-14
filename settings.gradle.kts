rootProject.name = "rental-warehouse-assistant-system"

includeBuild("build-logic")

include(
    ":api",
    ":shared",
    ":services:doc-service",
    ":services:estimate-draft-service",
    ":services:estimate-service",
    ":services:image-ya-disk-service",
    ":services:llm-service",
    ":services:s3-service",
    ":services:ui-service"
)
