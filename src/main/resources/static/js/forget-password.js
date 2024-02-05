function setDefaultValue() {
            var numberField = document.getElementById("employeeId");
            // Check if the field is empty or not a number
            if (numberField.value.trim() === '' || isNaN(numberField.value)) {
                // Set the value to 0
                numberField.value = 0;
            }
}